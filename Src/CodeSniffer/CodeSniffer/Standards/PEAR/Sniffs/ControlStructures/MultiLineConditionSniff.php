<?php
/**
 * PEAR_Sniffs_ControlStructures_MultiLineConditionSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: MultiLineConditionSniff.php,v 1.3 2009/05/05 06:08:54 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * PEAR_Sniffs_ControlStructures_MultiLineConditionSniff.
 *
 * Ensure multi-line IF conditions are defined correctly.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.0RC2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class PEAR_Sniffs_ControlStructures_MultiLineConditionSniff implements PHP_CodeSniffer_Sniff
{


    /**
     * Returns an array of tokens this test wants to listen for.
     *
     * @return array
     */
    public function register()
    {
        return array(T_IF);

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
        $tokens = $phpcsFile->getTokens();

        // We need to work out how far indented the if statement
        // itself is, so we can work out how far to indent conditions.
        $statementIndent = 0;
        for ($i = ($stackPtr - 1); $i >= 0; $i--) {
            if ($tokens[$i]['line'] !== $tokens[$stackPtr]['line']) {
                $i++;
                break;
            }
        }

        if ($i >= 0 && $tokens[$i]['code'] === T_WHITESPACE) {
            $statementIndent = strlen($tokens[$i]['content']);
        }

        // Each line between the parenthesis should be indented 4 spaces
        // and start with an operator.
        $openBracket  = $tokens[$stackPtr]['parenthesis_opener'];
        $closeBracket = $tokens[$stackPtr]['parenthesis_closer'];
        $lastLine     = $tokens[$openBracket]['line'];
        for ($i = ($openBracket + 1); $i < $closeBracket; $i++) {
            if ($tokens[$i]['line'] !== $lastLine) {
                if ($tokens[$i]['line'] === $tokens[$closeBracket]['line']) {
                    $next = $phpcsFile->findNext(T_WHITESPACE, $i, null, true);
                    if ($next !== $closeBracket) {
                        // CLosing bracket is on the same line as a condition.
                        $error = 'Closing parenthesis of a multi-line IF statement must be on a new line';
                        $phpcsFile->addError($error, $i);
                        $expectedIndent = ($statementIndent + 4);
                    } else {
                        // Closing brace needs to be indented to the same level
                        // as the function.
                        $expectedIndent = $statementIndent;
                    }
                } else {
                    $expectedIndent = ($statementIndent + 4);
                }

                // We changed lines, so this should be a whitespace indent token.
                if ($tokens[$i]['code'] !== T_WHITESPACE) {
                    $foundIndent = 0;
                } else {
                    $foundIndent = strlen($tokens[$i]['content']);
                }

                if ($expectedIndent !== $foundIndent) {
                    $error = "Multi-line IF statement not indented correctly; expected $expectedIndent spaces but found $foundIndent";
                    $phpcsFile->addError($error, $i);
                }

                if ($tokens[$i]['line'] !== $tokens[$closeBracket]['line']) {
                    $next = $phpcsFile->findNext(T_WHITESPACE, $i, null, true);
                    if (in_array($tokens[$next]['code'], PHP_CodeSniffer_Tokens::$booleanOperators) === false) {
                        $error = 'Each line in a multi-line IF statement must begin with a boolean operator';
                        $phpcsFile->addError($error, $i);
                    }
                }

                $lastLine = $tokens[$i]['line'];
            }//end if
        }//end for

        // The openning brace needs to be one space away
        // from the closing parenthesis.
        if ($tokens[($closeBracket + 1)]['code'] !== T_WHITESPACE) {
            $length = 0;
        } else {
            $length = strlen($tokens[($closeBracket + 1)]['content']);
        }

        if ($length !== 1) {
            $error = "There must be a single space between the closing parenthesis and the openning brace of a multi-line IF statement; found $length spaces";
            $phpcsFile->addError($error, ($closeBracket + 1));
        }

        // And just in case they do something funny before the brace...
        $next = $phpcsFile->findNext(T_WHITESPACE, ($closeBracket + 1), null, true);
        if ($next !== false && $tokens[$next]['code'] !== T_OPEN_CURLY_BRACKET) {
            $error = 'There must be a single space between the closing parenthesis and the openning brace of a multi-line IF statement';
            $phpcsFile->addError($error, $next);
        }

    }//end process()


}//end class

?>
