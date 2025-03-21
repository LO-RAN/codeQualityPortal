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
 * @version   CVS: $Id: EmptyStatementSniff.php,v 1.1 2008/02/06 02:38:36 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * This sniff class detected empty statement.
 *
 * This sniff implements the common algorithm for empty statement body detection.
 * A body is considered as empty if it is completely empty or it only contains
 * whitespace characters and|or comments.
 *
 * <code>
 * stmt {
 *   // foo
 * }
 * stmt (conditions) {
 *   // foo
 * }
 * </code>
 *
 * Statements covered by this sniff are <b>catch</b>, <b>do</b>, <b>else</b>,
 * <b>elsif</b>, <b>for</b>, <b>foreach<b>, <b>if</b>, <b>switch</b>, <b>try</b>
 * and <b>while</b>.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Manuel Pichler <mapi@manuel-pichler.de>
 * @copyright 2007-2008 Manuel Pichler. All rights reserved.
 * @license   http://www.opensource.org/licenses/bsd-license.php  BSD License
 * @version   Release: 1.2.0RC2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class Generic_Sniffs_CodeAnalysis_EmptyStatementSniff implements PHP_CodeSniffer_Sniff
{

    /**
     * List of block tokens that this sniff covers.
     *
     * The key of this hash identifies the required token while the boolean
     * value says mark an error or mark a warning.
     *
     * @type array<boolean>
     * @var array(integer=>boolean) $_tokens
     */
    private $_tokens = array(
                        T_CATCH   => true,
                        T_DO      => false,
                        T_ELSE    => false,
                        T_ELSEIF  => false,
                        T_FOR     => false,
                        T_FOREACH => false,
                        T_IF      => false,
                        T_SWITCH  => false,
                        T_TRY     => false,
                        T_WHILE   => false,
                       );


    /**
     * Registers the tokens that this sniff wants to listen for.
     *
     * @return array(integer)
     */
    public function register()
    {
        return array_keys($this->_tokens);

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

        // Skip for-statements without body.
        if (isset($token['scope_opener']) === false) {
            return;
        }

        $next = ++$token['scope_opener'];
        $end  = --$token['scope_closer'];

        $emptyBody = true;
        for (; $next <= $end; ++$next) {
            if (in_array($tokens[$next]['code'], PHP_CodeSniffer_Tokens::$emptyTokens) === false) {
                $emptyBody = false;
                break;
            }
        }

        if ($emptyBody === true) {
            // Get token identifier.
            $name  = $phpcsFile->getTokensAsString($stackPtr, 1);
            $error = sprintf('Empty %s statement detected', strtoupper($name));
            if ($this->_tokens[$token['code']] === true) {
                $phpcsFile->addError($error, $stackPtr);
            } else {
                $phpcsFile->addWarning($error, $stackPtr);
            }
        }

    }//end process()


}//end class

?>