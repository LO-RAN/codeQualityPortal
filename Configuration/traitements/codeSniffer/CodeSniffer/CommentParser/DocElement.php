<?php
/**
 * A DocElement represents a logical element within a Doc Comment.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: DocElement.php,v 1.3 2007/01/08 05:07:29 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * A DocElement represents a logical element within a Doc Comment.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.0.0
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
interface PHP_CodeSniffer_CommentParser_DocElement
{


    /**
     * Returns the name of the tag this element represents, omitting the @ symbol.
     *
     * @return string
     */
    public function getTag();


    /**
     * Returns the whitespace that exists before this element.
     *
     * @return string
     * @see getWhitespaceAfter()
     */
    public function getWhitespaceBefore();


    /**
     * Returns the whitespace that exists after this element.
     *
     * @return string
     * @see getWhitespaceBefore()
     */
    public function getWhitespaceAfter();


    /**
     * Returns the order that this element appears in the doc comment.
     *
     * The first element in the comment should have an order of 1.
     *
     * @return int
     */
    public function getOrder();


    /**
     * Returns the element that appears before this element.
     *
     * @return PHP_CodeSniffer_CommentParser_DocElement
     * @see getNextElement()
     */
    public function getPreviousElement();


    /**
     * Returns the element that appears after this element.
     *
     * @return PHP_CodeSniffer_CommentParser_DocElement
     * @see getPreviousElement()
     */
    public function getNextElement();


    /**
     * Returns the line that this element started on.
     *
     * @return int
     */
    public function getLine();


    /**
     * Returns the raw content of this element, ommiting the tag.
     *
     * @return string
     */
    public function getRawContent();


}//end interface

?>
