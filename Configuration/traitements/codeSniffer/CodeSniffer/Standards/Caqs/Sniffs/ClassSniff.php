<?php
class Caqs_Sniffs_ClassSniff implements PHP_CodeSniffer_Sniff
{

    /**
     * Returns an array of tokens this test wants to listen for.
     *
     * @return array
     */
    public function register()
    {
        return array(T_CLASS,
                T_INTERFACE);

    }//end register()


    /**
     * Processes this test, when one of its tokens is encountered.
     *
     * @param PHP_CodeSniffer_File $phpcsFile The file being scanned.
     * @param int                  $stackPtr  The position of the current token
     *                                        in the stack passed in $tokens.
     *
     * @return void
     */
    public function process(PHP_CodeSniffer_File $phpcsFile, $stackPtr)
    {    
        $this->currentFile = $phpcsFile;

        $tokens = $phpcsFile->getTokens();

        // Detect start and end of this function definition.
        $start = $tokens[$stackPtr]['scope_opener'];
        $end   = $tokens[$stackPtr]['scope_closer'];

        $phpcsFile->addVolumetry('', $stackPtr);

        return;

    }//end process()


}//end class
?>