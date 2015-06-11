<?php

error_reporting(E_ERROR | E_WARNING | E_PARSE);
ini_set("memory_limit",'1G');

spl_autoload_register(array('PHP_CodeSniffer', 'autoload'));

if (class_exists('PHP_CodeSniffer_Exception', true) === false) {
    throw new Exception('Class PHP_CodeSniffer_Exception not found');
}

if (class_exists('PHP_CodeSniffer_File', true) === false) {
    throw new PHP_CodeSniffer_Exception('Class PHP_CodeSniffer_File not found');
}

if (class_exists('PHP_CodeSniffer_Tokens', true) === false) {
    throw new PHP_CodeSniffer_Exception('Class PHP_CodeSniffer_Tokens not found');
}

if (interface_exists('PHP_CodeSniffer_Sniff', true) === false) {
    throw new PHP_CodeSniffer_Exception('Interface PHP_CodeSniffer_Sniff not found');
}

if (interface_exists('PHP_CodeSniffer_MultiFileSniff', true) === false) {
    throw new PHP_CodeSniffer_Exception('Interface PHP_CodeSniffer_MultiFileSniff not found');
}

class CaqsOutputMng
{
	private $outputfile = NULL;
	private $srcDir = NULL;
	
    /**
     * The file or directory that is currently being processed.
     *
     * @var string
     */
    protected $file = array();

    /**
     * The directory to search for sniffs in.
     *
     * @var string
     */
    protected $standardDir = '';

    /**
     * The files that have been processed.
     *
     * @var array(PHP_CodeSniffer_File)
     */
    protected $files = array();

    /**
     * The CLI object controlling the run.
     *
     * @var string
     */
    protected $cli = null;

    /**
     * The path that that PHP_CodeSniffer is being run from.
     *
     * Stored so that the path can be restored after it is changed
     * in the constructor.
     *
     * @var string
     */
    private $_cwd = null;

    /**
     * The listeners array.
     *
     * @var array(PHP_CodeSniffer_Sniff)
     */
    protected $listeners = array();

    /**
     * The listeners array, indexed by token type.
     *
     * @var array()
     */
    private $_tokenListeners = array(
                                'file'      => array(),
                                'multifile' => array(),
                               );

    /**
     * An array of patterns to use for skipping files.
     *
     * @var array()
     */
    protected $ignorePatterns = array();

    /**
     * An array of extensions for files we will check.
     *
     * @var array
     */
    public $allowedFileExtensions = array(
                                     'php' => 'PHP',
                                     'inc' => 'PHP',
                                     'js'  => 'JS',
                                     'css' => 'CSS',
                                    );

    /**
     * An array of variable types for param/var we will check.
     *
     * @var array(string)
     */
    public static $allowedTypes = array(
                                   'array',
                                   'boolean',
                                   'float',
                                   'integer',
                                   'mixed',
                                   'object',
                                   'string',
                                  );
							   
							   /**
     * Autoload static method for loading classes and interfaces.
     *
     * @param string $className The name of the class or interface.
     *
     * @return void
     */
    public static function autoload($className)
    {
        if (substr($className, 0, 4) === 'PHP_') {
            $newClassName = substr($className, 4);
        } else {
            $newClassName = $className;
        }

        $path = str_replace('_', '/', $newClassName).'.php';

        if (is_file(dirname(__FILE__).'/'.$path) === true) {
            // Check standard file locations based on class name.
            include dirname(__FILE__).'/'.$path;
        } else if (is_file(dirname(__FILE__).'/CodeSniffer/Standards/'.$path) === true) {
            // Check for included sniffs.
            include dirname(__FILE__).'/CodeSniffer/Standards/'.$path;
        } else {
            // Everything else.
            @include $path;
        }

    }//end autoload()


    /**
     * Sets an array of file extensions that we will allow checking of.
     *
     * If the extension is one of the defaults, a specific tokenizer
     * will be used. Otherwise, the PHP tokenizer will be used for
     * all extensions passed.
     *
     * @param array $extensions An array of file extensions.
     *
     * @return void
     */
    public function setAllowedFileExtensions(array $extensions)
    {
        $newExtensions = array();
        foreach ($extensions as $ext) {
            if (isset($this->allowedFileExtensions[$ext]) === true) {
                $newExtensions[$ext] = $this->allowedFileExtensions[$ext];
            } else {
                $newExtensions[$ext] = 'PHP';
            }
        }

        $this->allowedFileExtensions = $newExtensions;

    }//end setAllowedFileExtensions()


    /**
     * Sets an array of ignore patterns that we use to skip files and folders.
     *
     * Patterns are not case sensitive.
     *
     * @param array $patterns An array of ignore patterns.
     *
     * @return void
     */
    public function setIgnorePatterns(array $patterns)
    {
        $this->ignorePatterns = $patterns;

    }//end setIgnorePatterns()


    /**
     * Sets the internal CLI object.
     *
     * @param object $cli The CLI object controlling the run.
     *
     * @return void
     */
    public function setCli($cli)
    {
        $this->cli = $cli;

    }//end setCli()


    /**
     * Adds a file to the list of checked files.
     *
     * Checked files are used to generate error reports after the run.
     *
     * @param PHP_CodeSniffer_File $phpcsFile The file to add.
     *
     * @return void
     */
    public function addFile(PHP_CodeSniffer_File $phpcsFile)
    {
        $this->files[] = $phpcsFile;

    }//end addFile()
	/**
     * Sets installed sniffs in the coding standard being used.
     *
     * @param string $standard The name of the coding standard we are checking.
     * @param array  $sniffs   The sniff names to restrict the allowed
     *                         listeners to.
     *
     * @return null
     */
    public function setTokenListeners($standard, array $sniffs=array())
    {
        $this->listeners = $this->getTokenListeners($standard, $sniffs);

    }//end setTokenListeners()
	
	/**
     * Gets installed sniffs in the coding standard being used.
     *
     * Traverses the standard directory for classes that implement the
     * PHP_CodeSniffer_Sniff interface asks them to register. Each of the
     * sniff's class names must be exact as the basename of the sniff file.
     *
     * Returns an array of sniff class names.
     *
     * @param string $standard The name of the coding standard we are checking.
     * @param array  $sniffs   The sniff names to restrict the allowed
     *                         listeners to.
     *
     * @return array
     * @throws PHP_CodeSniffer_Exception If any of the tests failed in the
     *                                   registration process.
     */
    public function getTokenListeners($standard, array $sniffs=array())
    {
	    if (is_dir($standard) === true) {
            // This is an absolute path to a custom standard.
            $this->standardDir = $standard;
            $standard          = basename($standard);
			echo $standard.PHP_EOL;
        } else {
            $this->standardDir = realpath(dirname(__FILE__).'/../Standards/'.$standard);//DAZ
	        if (is_dir($this->standardDir) === false) {
	            // This isn't looking good. Let's see if this
                // is a relative path to a custom standard.
                if (is_dir(realpath($this->_cwd.'/'.$standard)) === true) {
                    // This is a relative path to a custom standard.
                    $this->standardDir = realpath($this->_cwd.'/'.$standard);
                    $standard          = basename($standard);
                }
            }
        }

        $files = self::getSniffFiles($this->standardDir, $standard);

        if (empty($sniffs) === false) {
            // Convert the allowed sniffs to lower case so
            // they are easier to check.
            foreach ($sniffs as &$sniff) {
                $sniff = strtolower($sniff);
            }
        }

        $listeners = array();

        foreach ($files as $file) {
            // Work out where the position of /StandardName/Sniffs/... is
            // so we can determine what the class will be called.
            $sniffPos = strrpos($file, DIRECTORY_SEPARATOR.'Sniffs'.DIRECTORY_SEPARATOR);
            if ($sniffPos === false) {
                continue;
            }

            $slashPos = strrpos(substr($file, 0, $sniffPos), DIRECTORY_SEPARATOR);
            if ($slashPos === false) {
                continue;
            }

            $className = substr($file, ($slashPos + 1));
            $className = substr($className, 0, -4);
            $className = str_replace(DIRECTORY_SEPARATOR, '_', $className);

            include_once $file;

            // If they have specified a list of sniffs to restrict to, check
            // to see if this sniff is allowed.
            $allowed = in_array(strtolower($className), $sniffs);
            if (empty($sniffs) === false && $allowed === false) {
                continue;
            }

            $listeners[] = $className;

            if (PHP_CODESNIFFER_VERBOSITY > 2) {
                echo "\tRegistered $className".PHP_EOL;
            }
        }//end foreach

        return $listeners;

    }//end getTokenListeners()
	
    /**
     * Constructs a PHP_CodeSniffer object.
     *
     * @param int  $verbosity   The verbosity level.
     *                          1: Print progress information.
     *                          2: Print developer debug information.
     * @param int  $tabWidth    The number of spaces each tab represents.
     *                          If greater than zero, tabs will be replaced
     *                          by spaces before testing each file.
     * @param bool $interactive If TRUE, will stop after each file with errors
     *                          and wait for user input.
     *
     * @see process()
     */
    public function __construct($verbosity=0, $tabWidth=0, $interactive=false)
    {
        if (defined('PHP_CODESNIFFER_VERBOSITY') === false) {
            define('PHP_CODESNIFFER_VERBOSITY', $verbosity);
        }

        if (defined('PHP_CODESNIFFER_TAB_WIDTH') === false) {
            define('PHP_CODESNIFFER_TAB_WIDTH', $tabWidth);
        }

        if (defined('PHP_CODESNIFFER_INTERACTIVE') === false) {
            define('PHP_CODESNIFFER_INTERACTIVE', $interactive);
        }

        // Change into a directory that we know about to stop any
        // relative path conflicts.
        $this->_cwd = getcwd();
        chdir(dirname(__FILE__).'/../');//DAZ

    }//end __construct()


    /**
     * Destructs a PHP_CodeSniffer object.
     *
     * Restores the current working directory to what it
     * was before we started our run.
     *
     * @return void
     */
    public function __destruct()
    {
        chdir($this->_cwd);

    }//end __destruct()


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
			$this->standardDir = realpath(dirname(__FILE__).'/CodeSniffer/Standards/'.$standard);
		}

        // Reset the members.
        $this->listeners       = array();
        $this->files           = array();
        $this->_tokenListeners = array(
                                  'file'      => array(),
                                  'multifile' => array(),
                                 );

        if (PHP_CODESNIFFER_VERBOSITY > 0) {
            echo "Registering sniffs in ".basename($standard)." standard... ";
            if (PHP_CODESNIFFER_VERBOSITY > 2) {
                echo PHP_EOL;
            }
        }

        $this->setTokenListeners($standard, $sniffs);
        if (PHP_CODESNIFFER_VERBOSITY > 0) {
            $numSniffs = count($this->listeners);
            echo "DONE ($numSniffs sniffs registered)".PHP_EOL;
        }

        $this->populateTokenListeners();

        // The SVN pre-commit calls process() to init the sniffs
        // so there may not be any files to process.
        if (empty($files) === true) {
            return;
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

        // Now process the multi-file sniffs, assuming there are
        // multiple files being sniffed.
        /*if (count($files) > 1 || is_dir($files[0]) === true) {
            foreach ($this->_tokenListeners['multifile'] as $listener) {
                // Set the name of the listener for error messages.
                $activeListener = get_class($listener);
                foreach ($this->files as $file) {
                    $file->setActiveListener($activeListener);
                }

                $listener->process($this->files);
            }
        }*/
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
	try {
            if ($local === true) {
                $di = new DirectoryIterator($dir);
            } else {
                $di = new RecursiveIteratorIterator(new RecursiveDirectoryIterator($dir));
            }

            foreach ($di as $file) {
                $filePath = realpath($file->getPathname());
                if ($filePath === false) {
                    continue;
                }

                if (is_dir($filePath) === true) {
                    continue;
                }

                // Check that the file's extension is one we are checking.
                // Note that because we are doing a whole directory, we
                // are strict about checking the extension and we don't
                // let files with no extension through.
                $fileParts = explode('.', $file);
                $extension = array_pop($fileParts);
                if ($extension === $file) {
                    continue;
                }

                if (isset($this->allowedFileExtensions[$extension]) === false) {
                    continue;
                }
                $this->processFile($filePath);
            }//end foreach
        } catch (Exception $e) {
            $trace = $e->getTrace();

            $filename = $trace[0]['args'][0];
            if (is_numeric($filename) === true) {
                // See if we can find the PHP_CodeSniffer_File object.
                foreach ($trace as $data) {
                    if (isset($data['args'][0]) === true && ($data['args'][0] instanceof PHP_CodeSniffer_File) === true) {
                        $filename = $data['args'][0]->getFilename();
                    }
                }
            }

            $error = 'An error occurred during processing; checking has been aborted. The error message was: '.$e->getMessage();
            $phpcsFile = new PHP_CodeSniffer_File($filename, $this->listeners, $this->allowedFileExtensions);
            $this->addFile($phpcsFile);
            $phpcsFile->addError($error, null);
            return;
        }
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
	protected function processFile($file, $contents=null)
	{
		if ($contents === null && file_exists($file) === false) {
            throw new PHP_CodeSniffer_Exception("Source file $file does not exist");
        }
		
		// If the file's path matches one of our ignore patterns, skip it.
        foreach ($this->ignorePatterns as $pattern) {
            $replacements = array(
                             '\\,' => ',',
                             '*'   => '.*',
                            );

            $pattern = strtr($pattern, $replacements);
            if (preg_match("|{$pattern}|i", $file) === 1) {
                return;
            }
        }

        // Before we go and spend time tokenizing this file, just check
        // to see if there is a tag up top to indicate that the whole
        // file should be ignored. It must be on one of the first two lines.
        $firstContent = $contents;
        if ($contents === null) {
            $handle = fopen($file, 'r');
            if ($handle !== false) {
                $firstContent  = fgets($handle);
                $firstContent .= fgets($handle);
                fclose($handle);
            }
        }

        if (strpos($firstContent, '@codingStandardsIgnoreFile') !== false) {
            // We are ignoring the whole file.
            if (PHP_CODESNIFFER_VERBOSITY > 0) {
                echo 'Ignoring '.basename($file).PHP_EOL;
            }

            return;
        }
		
		$this->_processFile($file, $contents);
		
	}//end processFile()
	
	/**
     * Process the sniffs for a single file.
     *
     * Does raw processing only. No interactive support or error checking.
     *
     * @param string $file     The file to process.
     * @param string $contents The contents to parse. If NULL, the content
     *                         is taken from the file system.
     *
     * @return void
     * @see    processFile()
     */
    private function _processFile($file, $contents)
    {
        if (PHP_CODESNIFFER_VERBOSITY > 0) {
            $startTime = time();
            echo 'Processing '.basename($file).' ';
            if (PHP_CODESNIFFER_VERBOSITY > 1) {
                echo PHP_EOL;
            }
        }

        $phpcsFile = new PHP_CodeSniffer_File(
            $file,
            $this->_tokenListeners['file'],
            $this->allowedFileExtensions
        );
        $this->addFile($phpcsFile);
        $phpcsFile->start($contents);
		
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
		$eltn = str_replace(".","_",$eltn);
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

        // Clean up the test if we can to save memory. This can't be done if
        // we need to leave the files around for multi-file sniffs.
        if (PHP_CODESNIFFER_INTERACTIVE === false
            && empty($this->_tokenListeners['multifile']) === true
        ) {
            $phpcsFile->cleanUp();
        }

        if (PHP_CODESNIFFER_VERBOSITY > 0) {
            $timeTaken = (time() - $startTime);
            if ($timeTaken === 0) {
                echo 'DONE in < 1 second';
            } else if ($timeTaken === 1) {
                echo 'DONE in 1 second';
            } else {
                echo "DONE in $timeTaken seconds";
            }

            $errors   = $phpcsFile->getErrorCount();
            $warnings = $phpcsFile->getWarningCount();
            echo " ($errors errors, $warnings warnings)".PHP_EOL;
        }

    }//end _processFile()
	
	/**
     * Return a list of sniffs that a coding standard has defined.
     *
     * Sniffs are found by recursing the standard directory and also by
     * asking the standard for included sniffs.
     *
     * @param string $dir      The directory where to look for the files.
     * @param string $standard The name of the coding standard. If NULL, no
     *                         included sniffs will be checked for.
     *
     * @return array
     * @throws PHP_CodeSniffer_Exception If an included or excluded sniff does
     *                                   not exist.
     */
    public static function getSniffFiles($dir, $standard=null)
    {
        $di = new RecursiveIteratorIterator(new RecursiveDirectoryIterator($dir));

        $ownSniffs      = array();
        $includedSniffs = array();
        $excludedSniffs = array();

        foreach ($di as $file) {
            $fileName = $file->getFilename();

            // Skip hidden files.
            if (substr($fileName, 0, 1) === '.') {
                continue;
            }

            // We are only interested in PHP and sniff files.
            $fileParts = explode('.', $fileName);
            if (array_pop($fileParts) !== 'php') {
                continue;
            }

            $basename = basename($fileName, '.php');
            if (substr($basename, -5) !== 'Sniff') {
                continue;
            }

            $ownSniffs[] = $file->getPathname();
        }//end foreach

        // Load the standard class and ask it for a list of external
        // sniffs to include in the standard.
        if ($standard !== null
            && is_file("$dir/{$standard}CodingStandard.php") === true
        ) {
            include_once "$dir/{$standard}CodingStandard.php";
            $standardClassName = "PHP_CodeSniffer_Standards_{$standard}_{$standard}CodingStandard";
            $standardClass     = new $standardClassName;

            $included = $standardClass->getIncludedSniffs();
            foreach ($included as $sniff) {
                if (is_dir($sniff) === true) {
                    // Trying to include from a custom standard.
                    $sniffDir = $sniff;
                    $sniff    = basename($sniff);
                } else if (is_file($sniff) === true) {
                    // Trying to include a custom sniff.
                    $sniffDir = $sniff;
                } else {
                    $sniffDir = realpath(dirname(__FILE__)."/../Standards/$sniff");//DAZ
                    if ($sniffDir === false) {
                        $error = "Included sniff $sniff does not exist";
                        throw new PHP_CodeSniffer_Exception($error);
                    }
                }

                if (is_dir($sniffDir) === true) {
                    if (self::isInstalledStandard($sniff) === true) {
                        // We are including a whole coding standard.
                        $includedSniffs = array_merge($includedSniffs, self::getSniffFiles($sniffDir, $sniff));
                    } else {
                        // We are including a whole directory of sniffs.
                        $includedSniffs = array_merge($includedSniffs, self::getSniffFiles($sniffDir));
                    }

                    // Remove sniff copies.
                    $includedSniffs = array_unique($includedSniffs);
                } else {
                    if (substr($sniffDir, -9) !== 'Sniff.php') {
                        $error = "Included sniff $sniff does not exist";
                        throw new PHP_CodeSniffer_Exception($error);
                    }

                    $includedSniffs[] = $sniffDir;
                }
            }//end foreach

            $excluded = $standardClass->getExcludedSniffs();
            foreach ($excluded as $sniff) {
                if (is_dir($sniff) === true) {
                    // Trying to exclude from a custom standard.
                    $sniffDir = $sniff;
                    $sniff    = basename($sniff);
                } else if (is_file($sniff) === true) {
                    // Trying to exclude a custom sniff.
                    $sniffDir = $sniff;
                } else {
                    $sniffDir = realpath(dirname(__FILE__)."/CodeSniffer/Standards/$sniff");
                    if ($sniffDir === false) {
                        $error = "Excluded sniff $sniff does not exist";
                        throw new PHP_CodeSniffer_Exception($error);
                    }
                }

                if (is_dir($sniffDir) === true) {
                    if (self::isInstalledStandard($sniff) === true) {
                        // We are excluding a whole coding standard.
                        $excludedSniffs = array_merge(
                            $excludedSniffs,
                            self::getSniffFiles($sniffDir, $sniff)
                        );
                    } else {
                        // We are excluding a whole directory of sniffs.
                        $excludedSniffs = array_merge(
                            $excludedSniffs,
                            self::getSniffFiles($sniffDir)
                        );
                    }
                } else {
                    if (substr($sniffDir, -9) !== 'Sniff.php') {
                        $error = "Excluded sniff $sniff does not exist";
                        throw new PHP_CodeSniffer_Exception($error);
                    }

                    $excludedSniffs[] = $sniffDir;
                }
            }//end foreach
        }//end if

        // Merge our own sniff list with our externally included
        // sniff list, but filter out any excluded sniffs.
        $files = array();
        foreach (array_merge($ownSniffs, $includedSniffs) as $sniff) {
            if (in_array($sniff, $excludedSniffs) === true) {
                continue;
            } else {
                $files[] = $sniff;
            }
        }

        return $files;

    }//end getSniffFiles()
	
	
    /**
     * Populates the array of PHP_CodeSniffer_Sniff's for this file.
     *
     * @return void
     */
    public function populateTokenListeners()
    {
        // Construct a list of listeners indexed by token being listened for.
        $this->_tokenListeners = array(
                                  'file'      => array(),
                                  'multifile' => array(),
                                 );

        foreach ($this->listeners as $listenerClass) {
            $listener = new $listenerClass();

            if (($listener instanceof PHP_CodeSniffer_Sniff) === true) {
                $tokens = $listener->register();
                if (is_array($tokens) === false) {
                    $msg = "Sniff $listenerClass register() method must return an array";
                    throw new PHP_CodeSniffer_Exception($msg);
                }

                foreach ($tokens as $token) {
                    if (isset($this->_tokenListeners['file'][$token]) === false) {
                        $this->_tokenListeners['file'][$token] = array();
                    }

                    if (in_array($listener, $this->_tokenListeners['file'][$token], true) === false) {
                        $this->_tokenListeners['file'][$token][] = $listener;
                    }
                }
            } else if (($listener instanceof PHP_CodeSniffer_MultiFileSniff) === true) {
                $this->_tokenListeners['multifile'][] = $listener;
            }
        }//end foreach

    }//end populateTokenListeners()
	
	/**
     * Get a list of all coding standards installed.
     *
     * Coding standards are directories located in the
     * CodeSniffer/Standards directory. Valid coding standards
     * include a Sniffs subdirectory.
     *
     * @param boolean $includeGeneric If true, the special "Generic"
     *                                coding standard will be included
     *                                if installed.
     * @param string  $standardsDir   A specific directory to look for standards
     *                                in. If not specified, PHP_CodeSniffer will
     *                                look in its default location.
     *
     * @return array
     * @see isInstalledStandard()
     */
    public static function getInstalledStandards(
        $includeGeneric=false,
        $standardsDir=''
    ) {
        $installedStandards = array();

        if ($standardsDir === '') {
            $standardsDir = dirname(__FILE__).'/CodeSniffer/Standards';
        }

        $di = new DirectoryIterator($standardsDir);
        foreach ($di as $file) {
            if ($file->isDir() === true && $file->isDot() === false) {
                $filename = $file->getFilename();

                // Ignore the special "Generic" standard.
                if ($includeGeneric === false && $filename === 'Generic') {
                    continue;
                }

                // Valid coding standard dirs include a standard class.
                $csFile = $file->getPathname()."/{$filename}CodingStandard.php";
                if (is_file($csFile) === true) {
                    // We found a coding standard directory.
                    $installedStandards[] = $filename;
                }
            }
        }

        return $installedStandards;

    }//end getInstalledStandards()


    /**
     * Determine if a standard is installed.
     *
     * Coding standards are directories located in the
     * CodeSniffer/Standards directory. Valid coding standards
     * include a Sniffs subdirectory.
     *
     * @param string $standard The name of the coding standard.
     *
     * @return boolean
     * @see getInstalledStandards()
     */
    public static function isInstalledStandard($standard)
    {
        $standardDir  = dirname(__FILE__);
        $standardDir .= '/../Standards/'.$standard;
        if (is_file("$standardDir/{$standard}CodingStandard.php") === true) {
            return true;
        } else {
            // This could be a custom standard, installed outside our
            // standards directory.
            $standardFile = rtrim($standard, ' /\\').DIRECTORY_SEPARATOR.basename($standard).'CodingStandard.php';
            return (is_file($standardFile) === true);
        }

    }//end isInstalledStandard()
	
}
?>