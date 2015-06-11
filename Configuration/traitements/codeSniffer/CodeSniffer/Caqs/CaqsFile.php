<?php
class CaqsFile extends PHP_CodeSniffer_File
{
	private $currentListener = NULL;
	
	protected $_volumetry = array();
	
	protected function getClassName() {
		$retour = 'CS_'.get_class($this->currentListener);
		/*if($retour=='CS_Caqs_Sniffs_Metrics_CodeLineCountSniff') {
			$retour = 'LOC';
		} else if($retour=='CS_Caqs_Sniffs_Metrics_CommentLineCountSniff') {
			$retour = 'CLOC';
		} else if($retour=='CS_Caqs_Sniffs_Metrics_CyclomaticComplexitySniff') {
			$retour = 'VG';
		} else if($retour=='CS_Caqs_Sniffs_Metrics_DestructurationSniff') {
			$retour = 'EVG';
		} else if($retour=='CS_Caqs_Sniffs_Metrics_NestingLevelSniff') {
			$retour = 'nestingLevel';
		}*/
		return $retour;
	}
	
	public function getFullQualifiedFunctionName($stackPtr) {
		$token = $this->findNext(T_STRING, $stackPtr);
		$retour = $this->_tokens[$token]['content'];
		$functionLevel = $this->_tokens[$token]['level'];
		$s = $stackPtr;
		$thislevel = 0;
		do {
			$s = $s-1;
			$tC = $this->_tokens[$s]['code'];
			$classLevel = $this->_tokens[$s]['level'];
			if($tC==T_OPEN_CURLY_BRACKET) {
				$thislevel++;
			}
			if($tC==T_CLOSE_CURLY_BRACKET) {
				$thislevel--;
			}
		} while($tC!==T_CLASS && $tC!==T_INTERFACE && $s>0);
		if(($tC===T_CLASS || $tC===T_INTERFACE) && ($thislevel==1) ) {
			$token = $this->findNext(T_STRING, $s);
			$retour = $this->_tokens[$token]['content'].'.'.$retour;
		}
		return $retour;
	}
	
    public function getDeclarationName($stackPtr)
    {
        $tokenCode = $this->_tokens[$stackPtr]['code'];
        if ($tokenCode !== T_FUNCTION && $tokenCode !== T_CLASS && $tokenCode !== T_INTERFACE) {
            throw new PHP_CodeSniffer_Exception('Token type is not T_FUNCTION, T_CLASS OR T_INTERFACE');
        }
		$retour = "";
		if($tokenCode===T_CLASS || $tokenCode===T_INTERFACE) {
        	$token = $this->findNext(T_STRING, $stackPtr);
        	$retour = $this->_tokens[$token]['content'];
		} else if($tokenCode===T_FUNCTION){
			$retour = $this->getFullQualifiedFunctionName($stackPtr);
		}
        return $retour;

    }
	
	    /**
     * Adds an error to the error stack.
     *
     * @param string $error    The error message.
     * @param int    $stackPtr The stack position where the error occured.
     *
     * @return void
     * @throws PHP_CodeSniffer_Exception If $stackPtr is null.
     */
    public function addError($error, $stackPtr)
    {
        if ($stackPtr === null) {
            throw new PHP_CodeSniffer_Exception('$stackPtr cannot be null');
        }

        $lineNum = $this->_tokens[$stackPtr]['line'];
        $column  = $this->_tokens[$stackPtr]['column'];

        if (isset($this->_errors[$lineNum]) === false) {
            $this->errors[$lineNum] = array();
        }

        if (isset($this->_errors[$lineNum][$column]) === false) {
            $this->errors[$lineNum][$column] = array();
        }

        $tokenCode = $this->_tokens[$stackPtr]['code'];
        $eltName = $this->getFilename();
//        $eltName = str_replace(".","_",$eltName);
        $eltName = str_replace(".php","",$eltName);
        $type = 'FILE';
        if ($tokenCode === T_FUNCTION || $tokenCode === T_CLASS || $tokenCode === T_INTERFACE) {
        	$eltName .= '.'.$this->getDeclarationName($stackPtr);
        	if($tokenCode===T_FUNCTION) {
        		$type = 'MET';
        	} else {
        		$type = 'CLS';
        	}
        }
        $this->_errors[$lineNum][$column][] = array(
        	'messageId'  => $this->getClassName(),
        	'eltName' => $eltName,
        	'path'	=> $this->getFilename(),
        	'eltType' => $type,
        	'value'	=> -1
        );
        $this->_errorCount++;

    }//end addError()

    public function addVolumetry($volumetry, $stackPtr)
    {
        if ($stackPtr === null) {
            throw new PHP_CodeSniffer_Exception('$stackPtr cannot be null');
        }

        $lineNum = $this->_tokens[$stackPtr]['line'];

        if (isset($this->_volumetry[$lineNum]) === false) {
            $this->_volumetry[$lineNum] = array();
        }

        $tokenCode = $this->_tokens[$stackPtr]['code'];
        $eltName = $this->getFilename();
//        $eltName = str_replace(".","_",$eltName);
        $eltName = str_replace(".php","",$eltName);
        $type = 'FILE';
        if ($tokenCode === T_FUNCTION || $tokenCode === T_CLASS || $tokenCode === T_INTERFACE) {
        	$eltName .= '.'.$this->getDeclarationName($stackPtr);
        	if($tokenCode===T_FUNCTION) {
        		$type = 'MET';
        	} else {
        		$type = 'CLS';
        	}
        }
        $this->_volumetry[$lineNum][] = array(
        	'messageId'  => $this->getClassName(),
        	'eltName' => $eltName,
        	'path'	=> $this->getFilename(),
        	'eltType' => $type,
        	'value'	=> -1
		);
    }//end addError()
    
    public function getVolumetry()
    {
        return $this->_volumetry;
    }//end getVolumetry()

    /**
     * Adds an warning to the warning stack.
     *
     * @param string $warning  The error message.
     * @param int    $stackPtr The stack position where the error occured.
     *
     * @return void
     * @throws PHP_CodeSniffer_Exception If $stackPtr is null.
     */
    public function addWarning($warning, $stackPtr)
    {
        if ($stackPtr === null) {
            throw new PHP_CodeSniffer_Exception('$stackPtr cannot be null');
        }

        $lineNum = $this->_tokens[$stackPtr]['line'];
        $column  = $this->_tokens[$stackPtr]['column'];

        if (isset($this->_warnings[$lineNum]) === false) {
            $this->_warnings[$lineNum] = array();
        }

        if (isset($this->_warnings[$lineNum][$column]) === false) {
            $this->_warnings[$lineNum][$column] = array();
        }

        $tokenCode = $this->_tokens[$stackPtr]['code'];
        $eltName = $this->getFilename();
//        $eltName = str_replace(".","_",$eltName);
        $eltName = str_replace(".php","",$eltName);
        $type = 'FILE';
        if ($tokenCode === T_FUNCTION || $tokenCode === T_CLASS || $tokenCode === T_INTERFACE) {
        	$eltName .= '.'.$this->getDeclarationName($stackPtr);
        	if($tokenCode===T_FUNCTION) {
        		$type = 'MET';
        	} else {
        		$type = 'CLS';
        	}
        }
        $this->_warnings[$lineNum][$column][] = array(
        	'messageId'  => $this->getClassName(),
        	'eltName' => $eltName,
        	'path'	=> $this->getFilename(),
        	'eltType' => $type,
        	'value'	=> -1
                );
        $this->_warningCount++;

    }//end addWarning()

    /**
     * Adds an info to the info stack.
     *
     * @param string $info  The info message.
     * @param int    $stackPtr The stack position where the info occured.
     *
     * @return void
     * @throws PHP_CodeSniffer_Exception If $stackPtr is null.
     */
    public function addInfo($info, $stackPtr)
    {
        if ($stackPtr === null) {
            throw new PHP_CodeSniffer_Exception('$stackPtr cannot be null');
        }

        $lineNum = $this->_tokens[$stackPtr]['line'];
        $column  = $this->_tokens[$stackPtr]['column'];

        if (isset($this->_infos[$lineNum]) === false) {
            $this->_infos[$lineNum] = array();
        }

        if (isset($this->_infos[$lineNum][$column]) === false) {
            $this->_infos[$lineNum][$column] = array();
        }

        $tokenCode = $this->_tokens[$stackPtr]['code'];
        $eltName = $this->getFilename();
//        $eltName = str_replace(".","_",$eltName);
        $eltName = str_replace(".php","",$eltName);
        $type = 'FILE';
        if ($tokenCode === T_FUNCTION || $tokenCode === T_CLASS || $tokenCode === T_INTERFACE) {
        	$eltName .= '.'.$this->getDeclarationName($stackPtr);
        	if($tokenCode===T_FUNCTION) {
        		$type = 'MET';
        	} else {
        		$type = 'CLS';
        	}
        }
        $this->_infos[$lineNum][$column][] = array(
        	'messageId'  => $this->getClassName(),
        	'eltName' => $eltName,
        	'path'	=> $this->getFilename(),
        	'eltType' => $type,
        	'value'	=> -1
        );
        $this->_infoCount++;

    }//end addInfo()
    
	public function addInfoWithValue($info, $stackPtr, $value)
    {
        if ($stackPtr === null) {
            throw new PHP_CodeSniffer_Exception('$stackPtr cannot be null');
        }

        $lineNum = $this->_tokens[$stackPtr]['line'];
        $column  = $this->_tokens[$stackPtr]['column'];

        if (isset($this->_infos[$lineNum]) === false) {
            $this->_infos[$lineNum] = array();
        }

        if (isset($this->_infos[$lineNum][$column]) === false) {
            $this->_infos[$lineNum][$column] = array();
        }

        $tokenCode = $this->_tokens[$stackPtr]['code'];
        $eltName = $this->getFilename();
//        $eltName = str_replace(".","_",$eltName);
        $eltName = str_replace(".php","",$eltName);
        $type = 'FILE';
        if ($tokenCode === T_FUNCTION || $tokenCode === T_CLASS || $tokenCode === T_INTERFACE) {
        	$eltName .= '.'.$this->getDeclarationName($stackPtr);
        	if($tokenCode===T_FUNCTION) {
        		$type = 'MET';
        	} else {
        		$type = 'CLS';
        	}
        }
        $this->_infos[$lineNum][$column][] = array(
        	'messageId'  => $this->getClassName(),
        	'eltName' => $eltName,
        	'path'	=> $this->getFilename(),
        	'eltType' => $type,
        	'value'	=> $value
        );
        $this->_infoCount++;

    }
    
        /**
     * Starts the stack traversal and tells listeners when tokens are found.
     *
     * @return void
     */
    public function start()
    {
        if (PHP_CODESNIFFER_VERBOSITY > 2) {
            echo "\t*** START TOKEN PROCESSING ***".PHP_EOL;
        }

        // Foreach of the listeners that have registed to listen for this
        // token, get them to process it.
        
        foreach ($this->_tokens as $stackPtr => $token) {
            if (PHP_CODESNIFFER_VERBOSITY > 2) {
                $type    = $token['type'];
                $content = str_replace($this->eolChar, '\n', $token['content']);
                echo "\t\tProcess token $stackPtr: $type => $content".PHP_EOL;
            }

            $tokenType = $token['code'];
            if (isset($this->_listeners[$tokenType]) === true) {
                foreach ($this->_listeners[$tokenType] as $listener) {
                	$this->currentListener = $listener;
                    if (PHP_CODESNIFFER_VERBOSITY > 2) {
                        $startTime = microtime(true);
                        echo "\t\t\tProcessing ".get_class($listener).'... ';
                    }

                    $listener->process($this, $stackPtr);

                    if (PHP_CODESNIFFER_VERBOSITY > 2) {
                        $timeTaken = round((microtime(true) - $startTime), 4);
                        echo "DONE in $timeTaken seconds".PHP_EOL;
                    }
                }
            }
        }//end foreach

        if (PHP_CODESNIFFER_VERBOSITY > 2) {
            echo "\t*** END TOKEN PROCESSING ***".PHP_EOL;
        }

        // We don't need the tokens any more, so get rid of them
        // to save some memory.
        $this->_tokens    = null;
        $this->_listeners = null;

    }//end start()
    
}
?>
