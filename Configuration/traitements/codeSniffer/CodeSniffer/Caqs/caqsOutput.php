<?php
if (is_file(dirname(__FILE__).'/CaqsFile.php') === true) {
    include_once dirname(__FILE__).'/CaqsFile.php';
}

class CaqsOutputMng extends PHP_CodeSniffer
{
	private $outputfile = NULL;
	private $srcDir = NULL;
	protected $file = array();


	public function __construct($verbosity=0, $tabWidth=0) {
		define('PHP_CODESNIFFER_VERBOSITY', $verbosity);
        define('PHP_CODESNIFFER_TAB_WIDTH', $tabWidth);
		
	}

	public function myprocess($files, $standard, array $sniffs=array(), $local=false, $output)
	{
		$this->outputfile = fopen($output, "w+");
		$this->srcDir = $files[0];
		
		if (is_array($files) === false) {
			if (is_string($files) === false || $files === null) {
				throw new PHP_CodeSniffer_Exception('$file must be a string');
			}

			$files = array($files);
		}

		if (is_string($standard) === false || $standard === null) {
			throw new PHP_CodeSniffer_Exception('$standard must be a string');
		}

		if (is_dir($standard) === true) {
			// This is a custom standard.
			$this->standardDir = $standard;
			$standard          = basename($standard);
		} else {
			$this->standardDir = realpath(dirname(__FILE__).'/../Standards/'.$standard);
		}

		// Reset the members.
		$this->listeners = array();
		$this->files     = array();

		if (PHP_CODESNIFFER_VERBOSITY > 0) {
			echo "Registering sniffs in $standard standard... ";
			if (PHP_CODESNIFFER_VERBOSITY > 2) {
				echo PHP_EOL;
			}
		}

		$this->registerTokenListeners($standard, $sniffs);
		if (PHP_CODESNIFFER_VERBOSITY > 0) {
			$numSniffs = count($this->listeners);
			echo "DONE ($numSniffs sniffs registered)".PHP_EOL;
		}
        
        fwrite($this->outputfile, "<Result>".PHP_EOL);
		
		foreach ($files as $file) {
			$this->file = $file;
			if (is_dir($this->file) === true) {
				$this->processFiles($this->file, $local);
			} else {
				$this->processFile($this->file);
			}
		}
		
		fwrite($this->outputfile, '</Result>'.PHP_EOL);
		fclose($this->outputfile);
	}

	/**
	 * Run the code sniffs over each file in a given directory.
	 *
	 * Recusively reads the specified directory and performs the PHP_CodeSniffer
	 * sniffs on each source file found within the directories.
	 *
	 * @param string  $dir   The directory to process.
	 * @param boolean $local If true, only process files in this directory, not
	 *                       sub directories.
	 *
	 * @return void
	 * @throws Exception If there was an error opening the directory.
	 */
	protected function processFiles($dir, $local=false)
	{
		if ($local === true) {
			$di = new DirectoryIterator($dir);
		} else {
			$di = new RecursiveIteratorIterator(new RecursiveDirectoryIterator($dir));
		}

		foreach ($di as $file) {
			$filePath = realpath($file->getPathname());

			if (is_dir($filePath) === true) {
				continue;
			}

			// Check that the file's extension is one we are checking.
			// Note that because we are doing a whole directory, we
			// are strick about checking the extension and we don't
			// let files with no extension through.
			$fileParts = explode('.', $file);
			$extension = array_pop($fileParts);
			if ($extension === $file) {
				continue;
			}

			if (in_array($extension, $this->allowedFileExtensions) === false) {
				continue;
			}

			$this->processFile($filePath);
		}//end foreach

	}//end processFiles()

	/**
	 * Run the code sniffs over a signle given file.
	 *
	 * Processes the file and runs the PHP_CodeSniffer sniffs to verify that it
	 * conforms with the standard.
	 *
	 * @param string $file The file to process.
	 *
	 * @return void
	 * @throws PHP_CodeSniffer_Exception If the file could not be processed.
	 */
	protected function processFile($file)
	{
		$file = realpath($file);

		if (file_exists($file) === false) {
			throw new PHP_CodeSniffer_Exception("Source file $file does not exist");
		}

		// If the file's path matches one of our ignore patterns, skip it.
		foreach ($this->ignorePatterns as $pattern) {
			$replacements = array('\\,' => ',', '*' => '.*');
			$pattern      = strtr($pattern, $replacements);
			if (preg_match("|{$pattern}|i", $file) === 1) {
				return;
			}
		}

		$filename    = $file;
		$phpcsFile     = new CaqsFile($file, $this->listeners);
		$phpcsFile->start();
		
		$infos       = $phpcsFile->getInfos();
		$warnings    = $phpcsFile->getWarnings();
		$errors      = $phpcsFile->getErrors();
		$numInfos    = $phpcsFile->getInfoCount();
		$numWarnings = $phpcsFile->getWarningCount();
		$numErrors   = $phpcsFile->getErrorCount();
		$volumetry   = $phpcsFile->getVolumetry();

		$elts = array();
		//on ajoute le fichier courant dans les éléments
		$eltn = $file;
//		$eltn = str_replace(".","_",$eltn);
		$eltn = str_replace(".php","",$eltn);
		$elts[] = array(
					'eltName' => $eltn,
					'eltType' => 'FILE',
					'path'	  => $file,
					'errors'  => array(),
				);
		
		foreach ($volumetry as $line => $lineVolumetry) {
			foreach ($lineVolumetry as $column => $message) {
				$cptElt = 0;
				$indexElt = -1;
				foreach($elts as $elt) {
					if($elt['eltName']==$message['eltName']) {
						$indexElt = $cptElt;							
						break;
					}
					$cptElt++;
				}
				if($indexElt===-1) {
					$thiselt = array(
							'eltName' => $message['eltName'],
							'eltType' => $message['eltType'],
							'path'    => $message['path'],
							'errors' => array(),
						);
					$elts[] = $thiselt;
				}
			}
		}//end foreach
		
		// Merge errors, warnings and infos.
		foreach ($errors as $line => $lineErrors) {
			foreach ($lineErrors as $column => $colErrors) {
				foreach ($colErrors as $message) {
					$cpt = 0;
					$cptElt = 0;
					$indexErrors = -1;
					$indexElt = -1;
					foreach($elts as $elt) {
						if($elt['eltName']==$message['eltName']) {
							$indexElt = $cptElt;							
							break;
						}
						$cptElt++;
					}
					if($indexElt===-1) {
						$thiselt = array(
								'eltName' => $message['eltName'],
								'path'    => $message['path'],
								'eltType' => $message['eltType'],
								'errors' => array(),
							);
						$elts[] = $thiselt;
						$indexElt = sizeof($elts)-1;
					} else {
						foreach($elts[$indexElt]['errors'] as $err) {
							if($err['message']==$message['messageId']) {
								$indexErrors = $cpt;
								break;
							}
							$cpt++;
						}
					}

					if($indexErrors===-1) {
						$newErrors = array(
	                                        'message' => $message['messageId'],
	                                        'type'    => 'ERROR',
											'line'	  => $line,
											'value'   => ($message['value']==-1)?1:$message['value']
								);
						$elts[$indexElt]['errors'][] = $newErrors;
					} else {
						$elts[$indexElt]['errors'][$indexErrors]['line'] = $elts[$indexElt]['errors'][$indexErrors]['line'].','.$line;
						if($message['value']==-1) {
							$elts[$indexElt]['errors'][$indexErrors]['value']++;
						} else {
							$elts[$indexElt]['errors'][$indexErrors]['value'] += $message['value'];
						}
					}
				}
			}
		}//end foreach

		foreach ($warnings as $line => $lineWarnings) {
			foreach ($lineWarnings as $column => $colWarnings) {
				foreach ($colWarnings as $message) {
					$cpt = 0;
					$cptElt = 0;
					$indexErrors = -1;
					$indexElt = -1;
					foreach($elts as $elt) {
						if($elt['eltName']==$message['eltName']) {
							$indexElt = $cptElt;							
							break;
						}
						$cptElt++;
					}
					if($indexElt===-1) {
						$thiselt = array(
								'eltName' => $message['eltName'],
								'path'    => $message['path'],
								'eltType' => $message['eltType'],
								'errors' => array(),
							);
						$elts[] = $thiselt;
						$indexElt = sizeof($elts)-1;
					} else {
						foreach($elts[$indexElt]['errors'] as $err) {
							if($err['message']==$message['messageId']) {
								$indexErrors = $cpt;
								break;
							}
							$cpt++;
						}
					}

					if($indexErrors===-1) {
						$newErrors = array(
	                                        'message' => $message['messageId'],
	                                        'type'    => 'WARNING',
											'line'	  => $line,
											'value'   => ($message['value']==-1)?1:$message['value']
								);
						$elts[$indexElt]['errors'][] = $newErrors;
					} else {
						$elts[$indexElt]['errors'][$indexErrors]['line'] = $elts[$indexElt]['errors'][$indexErrors]['line'].','.$line;
						if($message['value']==-1) {
							$elts[$indexElt]['errors'][$indexErrors]['value']++;
						} else {
							$elts[$indexElt]['errors'][$indexErrors]['value'] += $message['value'];
						}
					}
				}
			}
		}//end foreach
		foreach ($infos as $line => $lineInfos) {
			foreach ($lineInfos as $column => $colInfos) {
				foreach ($colInfos as $message) {
					$cpt = 0;
					$cptElt = 0;
					$indexErrors = -1;
					$indexElt = -1;
					foreach($elts as $elt) {
						if($elt['eltName']==$message['eltName']) {
							$indexElt = $cptElt;							
							break;
						}
						$cptElt++;
					}
					if($indexElt===-1) {
						$thiselt = array(
								'eltName' => $message['eltName'],
								'path'    => $message['path'],
								'eltType' => $message['eltType'],
								'errors' => array(),
							);
						$elts[] = $thiselt;
						$indexElt = sizeof($elts)-1;
					} else {
						foreach($elts[$indexElt]['errors'] as $err) {
							if($err['message']==$message['messageId']) {
								$indexErrors = $cpt;
								break;
							}
							$cpt++;
						}
					}

					if($indexErrors===-1) {
						$newErrors = array(
	                                        'message' => $message['messageId'],
	                                        'type'    => 'INFO',
											'line'	  => $line,
											'value'   => ($message['value']==-1)?1:$message['value']
								);
						$elts[$indexElt]['errors'][] = $newErrors;
					} else {
						$elts[$indexElt]['errors'][$indexErrors]['line'] = $elts[$indexElt]['errors'][$indexErrors]['line'].','.$line;
						if($message['value']==-1) {
							$elts[$indexElt]['errors'][$indexErrors]['value']++;
						} else {
							$elts[$indexElt]['errors'][$indexErrors]['value'] += $message['value'];
						}
					}
				}
			}
		}//end foreach

        foreach ($elts as $elt) {
        	$name = $elt['eltName'];
        	$name = substr($name, strlen($this->srcDir));
        	$firstChar = substr($name, 0, 1);
			if($firstChar=='/' || $firstChar=='\\') {
				$name = substr($name, 1);
			}
			$name = str_replace("/", ".", $name);
			$name = str_replace("\\", ".", $name);
			fwrite($this->outputfile, " <elt type=\"".$elt['eltType']."\" name=\"".$name."\"");
			$path = $elt['path'];
			$path = substr($path, strlen($this->srcDir));
			$firstChar = substr($path, 0, 1);
			if($firstChar=='/' || $firstChar=='\\') {
				$path = substr($path, 1);
			}
			fwrite($this->outputfile, " filepath=\"".$path."\"");
			fwrite($this->outputfile, " >".PHP_EOL);
			if($elt['eltType']!=='FILE') {
				$lastindexof = strrpos($name, ".");
				if($lastindexof!==false) {
					fwrite($this->outputfile, " <parent name=\"");
					fwrite($this->outputfile, substr($name,0,$lastindexof));
					fwrite($this->outputfile, "\" />".PHP_EOL);
				}
			}
			foreach($elt['errors'] as $error) {
				fwrite($this->outputfile, "  <metric id=\"".strtoupper($error['message'])."\"");
				fwrite($this->outputfile, "  value=\"".$error['value']."\" lines=\"".$error['line']."\"");
				fwrite($this->outputfile, "/>".PHP_EOL);
			}
			fwrite($this->outputfile, " </elt>".PHP_EOL);
		}//end foreach
	
	}//end processFile()
	
}
?>
