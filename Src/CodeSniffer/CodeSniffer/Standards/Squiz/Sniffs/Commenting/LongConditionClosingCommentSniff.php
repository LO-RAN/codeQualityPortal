<?php
/**
 * Squiz_Sniffs_ControlStructures_LongConditionClosingCommentSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: LongConditionClosingCommentSniff.php,v 1.6 2008/08/28 04:04:02 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Squiz_Sniffs_ControlStructures_LongConditionClosingCommentSniff.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.0RC2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class Squiz_Sniffs_Commenting_LongConditionClosingCommentSniff implements PHP_CodeSniffer_Sniff
{

    /**
     * A list of tokenizers this sniff supports.
     *
     * @var array
     */
    public $supportedTokenizers = array(
                                   'PHP',
                                   'JS',
                                  );

    /**
     * The openers that we are interested in.
     *
     * @var array(int)
     */
    private static $_openers = array(
                                T_SWITCH,
                                T_IF,
                                T_FOR,
                                T_FOREACH,
                                T_WHILE,
                               );

    /**
     * The length that a code block must be before
     * requiring a closing comment.
     *
     * @var int
     */
    protected $lineLimit = 20;


    /**
     * Returns an array of tokens this test wants to listen for.
     *
     * @return array
     */
    public function register()
    {
        return array(T_CLOSE_CURLY_BRACKET);

    }//end register()


    /**
     * Processes this test, when one of its tokens is encountered.
     *
     * @param PHP_CodeSniffer_File $phpcsFile The file being scanned.
     * @param int                  $stackPtr  The position of the current token in the
     *                                        stack passed in $tokens.
     *
     * @return void
     */
    public function process(PHP_CodeSniffer_File $phpcsFile, $stackPtr)
    {
        $tokens = $phpcsFile->getTokens();

        if (isset($tokens[$stackPtr]['scope_condition']) === false) {
            // No scope condition. It is a function closer.
            return;
        }

        $startCondition = $tokens[$tokens[$stackPtr]['scope_condition']];
        $startBrace     = $tokens[$tokens[$stackPtr]['scope_opener']];
        $endBrace       = $tokens[$stackPtr];

        // We are only interested in some code blocks.
        if (in_array($startCondition['code'], self::$_openers) === false) {
            return;
        }

        if ($startCondition['code'] === T_IF) {
            // If this is actually and ELSE IF, skip it as the brace
            // will be checked by the original IF.
            $else = $phpcsFile->findPrevious(T_WHITESPACE, ($tokens[$stackPtr]['scope_condition'] - 1), null, true);
            if ($tokens[$else]['code'] === T_ELSE) {
                return;
            }

            // IF statements that have an ELSE block need to use
            // "end if" rather than "end else" or "end elseif".
            do {
                $nextToken = $phpcsFile->findNext(T_WHITESPACE, ($stackPtr + 1), null, true);
                if ($tokens[$nextToken]['code'] === T_ELSE || $tokens[$nextToken]['code'] === T_ELSEIF) {
                    // Check for ELSE IF (2 tokens) as opposed to ELSEIF (1 token).
                    if ($tokens[$nextToken]['code'] === T_ELSE && isset($tokens[$nextToken]['scope_closer']) === false) {
                        $nextToken = $phpcsFile->findNext(T_WHITESPACE, ($nextToken + 1), null, true);
                        if ($tokens[$nextToken]['code'] !== T_IF) {
                            break;
                        }
                    }

                    // The end brace becomes the ELSE's end brace.
                    $stackPtr = $tokens[$nextToken]['scope_closer'];
                    $endBrace = $tokens[$stackPtr];
                } else {
                    break;
                }
            } while (isset($tokens[$nextToken]['scope_closer']) === true);
        }//end if

        $lineDifference = ($endBrace['line'] - $startBrace['line']);
        if ($lineDifference < $this->lineLimit) {
            return;
        }

        $expected = '//end '.$startCondition['content'];

        $comment = $phpcsFile->findNext(array(T_COMMENT), $stackPtr, null, false);
        if (($comment === false) || ($tokens[$comment]['line'] !== $endBrace['line'])) {
            $error = "End comment for long condition not found; expected \"$expected\"";
            $phpcsFile->addError($error, $stackPtr);
            return;
        }

        if (($comment - $stackPtr) !== 1) {
            $error = "Space found before closing comment; expected \"$expected\"";
            $phpcsFile->addError($error, $stackPtr);
        }

        if ((strpos(trim($tokens[$comment]['content']), $expected)) === false) {
            $found = trim($tokens[$comment]['content']);
            $error = "Incorrect closing comment; expected \"$expected\" but found \"$found\"";
            $phpcsFile->addError($error, $stackPtr);
            return;
        }

    }//end process()


}//end class


?>
