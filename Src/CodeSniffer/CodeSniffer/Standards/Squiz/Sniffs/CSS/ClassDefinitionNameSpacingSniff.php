<?php
/**
 * Squiz_Sniffs_CSS_ClassDefinitionNameSpacingSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: ClassDefinitionNameSpacingSniff.php,v 1.2 2008/12/01 23:36:31 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Squiz_Sniffs_CSS_ClassDefinitionNameSpacingSniff.
 *
 * Ensure there are no blank lines between the names of classes/IDs.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.0RC2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class Squiz_Sniffs_CSS_ClassDefinitionNameSpacingSniff implements PHP_CodeSniffer_Sniff
{

    /**
     * A list of tokenizers this sniff supports.
     *
     * @var array
     */
    public $supportedTokenizers = array('CSS');


    /**
     * Returns the token types that this sniff is interested in.
     *
     * @return array(int)
     */
    public function register()
    {
        return array(T_OPEN_CURLY_BRACKET);

    }//end register()


    /**
     * Processes the tokens that this sniff is interested in.
     *
     * @param PHP_CodeSniffer_File $phpcsFile The file where the token was found.
     * @param int                  $stackPtr  The position in the stack where
     *                                        the token was found.
     *
     * @return void
     */
    public function process(PHP_CodeSniffer_File $phpcsFile, $stackPtr)
    {
        $tokens = $phpcsFile->getTokens();

        // Find the first blank line before this openning brace, unless we get
        // to another style definition, comment or the start of the file.
        $endTokens = array(
                      T_CLOSE_CURLY_BRACKET,
                      T_COMMENT,
                      T_OPEN_TAG,
                     );

        $foundContent = false;
        $currentLine  = $tokens[$stackPtr]['line'];
        for ($i = ($stackPtr - 1); $i >= 0; $i--) {
            if (in_array($tokens[$i]['code'], $endTokens) === true) {
                break;
            }

            if ($tokens[$i]['line'] === $currentLine) {
                if ($tokens[$i]['code'] !== T_WHITESPACE) {
                    $foundContent = true;
                }

                continue;
            }

            // We changed lines.
            if ($foundContent === false) {
                // Before we throw an error, make sure we are not looking
                // at a gap before the style definition.
                $prev = $phpcsFile->findPrevious(T_WHITESPACE, $i, null, true);
                if ($prev !== false
                    && in_array($tokens[$prev]['code'], $endTokens) === false
                ) {
                    $error = 'Blank lines are not allowed between class names';
                    $phpcsFile->addError($error, ($i + 1));
                }
                break;
            }

            $foundContent = false;
            $currentLine  = $tokens[$i]['line'];
        }//end for

    }//end process()


}//end class

?>