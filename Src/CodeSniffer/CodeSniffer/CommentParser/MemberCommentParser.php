<?php
/**
 * Parses class member comments.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: MemberCommentParser.php,v 1.10 2008/12/02 02:38:33 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

if (class_exists('PHP_CodeSniffer_CommentParser_ClassCommentParser', true) === false) {
    $error = 'Class PHP_CodeSniffer_CommentParser_ClassCommentParser not found';
    throw new PHP_CodeSniffer_Exception($error);
}

/**
 * Parses class member comments.
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
class PHP_CodeSniffer_CommentParser_MemberCommentParser extends PHP_CodeSniffer_CommentParser_ClassCommentParser
{

    /**
     * Represents a \@var tag in a member comment.
     *
     * @var PHP_CodeSniffer_CommentParser_SingleElement
     */
    private $_var = null;


    /**
     * Parses Var tags.
     *
     * @param array $tokens The tokens that represent this tag.
     *
     * @return PHP_CodeSniffer_CommentParser_SingleElement
     */
    protected function parseVar($tokens)
    {
        $this->_var = new PHP_CodeSniffer_CommentParser_SingleElement(
            $this->previousElement,
            $tokens,
            'var',
            $this->phpcsFile
        );

        return $this->_var;

    }//end parseVar()


    /**
     * Returns the var tag found in the member comment.
     *
     * @return PHP_CodeSniffer_CommentParser_PairElement
     */
    public function getVar()
    {
        return $this->_var;

    }//end getVar()


    /**
     * Returns the allowed tags for this parser.
     *
     * @return array
     */
    protected function getAllowedTags()
    {
        return array('var' => true);

    }//end getAllowedTags()


}//end class

?>
