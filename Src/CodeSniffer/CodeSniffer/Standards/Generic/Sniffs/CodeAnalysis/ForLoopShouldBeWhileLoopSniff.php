<?php
/**
 * This file is part of the CodeAnalysis addon for PHP_CodeSniffer.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Manuel Pichler <mapi@manuel-pichler.de>
 * @copyright 2007-2008 Manuel Pichler. All rights reserved.
 * @license   http://www.opensource.org/licenses/bsd-license.php  BSD License
 * @version   CVS: $Id: ForLoopShouldBeWhileLoopSniff.php,v 1.1 2008/02/06 02:38:36 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Detects for-loops that can be simplified to a while-loop.
 *
 * This rule is based on the PMD rule catalog. Detects for-loops that can be
 * simplified as a while-loop.
 *
 * <code>
 * class Foo
 * {
 *     public function bar($x)
 *     {
 *         for (;true;) true; // No Init or Update part, may as well be: while (true)
 *     }
 * }
 * </code>
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Manuel Pichler <mapi@manuel-pichler.de>
 * @copyright 2007-2008 Manuel Pichler. All rights reserved.
 * @license   http://www.opensource.org/licenses/bsd-license.php  BSD License
 * @version   Release: 1.2.0RC2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class Generic_Sniffs_CodeAnalysis_ForLoopShouldBeWhileLoopSniff implements PHP_CodeSniffer_Sniff
{


    /**
     * Registers the tokens that this sniff wants to listen for.
     *
     * @return array(integer)
     */
    public function register()
    {
        return array(T_FOR);

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
        $token  = $tokens[$stackPtr];

        // Skip invalid statement.
        if (isset($token['parenthesis_opener']) === false) {
            return;
        }

        $next = ++$token['parenthesis_opener'];
        $end  = --$token['parenthesis_closer'];

        $parts = array(0, 0, 0);
        $index = 0;

        for (; $next <= $end; ++$next) {
            $code = $tokens[$next]['code'];
            if ($code === T_SEMICOLON) {
                ++$index;
            } else if (in_array($code, PHP_CodeSniffer_Tokens::$emptyTokens) === false) {
                ++$parts[$index];
            }
        }

        if ($parts[0] === 0 && $parts[2] === 0 && $parts[1] > 0) {
            $error = 'This FOR loop can be simplified to a WHILE loop';
            $phpcsFile->addWarning($error, $stackPtr);
        }

    }//end process()


}//end class

?>