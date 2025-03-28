<?php
/**
 * Processes pattern strings and checks that the code conforms to the pattern.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: AbstractPatternSniff.php,v 1.26 2008/12/11 06:08:10 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

if (class_exists('PHP_CodeSniffer_Standards_IncorrectPatternException', true) === false) {
    $error = 'Class PHP_CodeSniffer_Standards_IncorrectPatternException not found';
    throw new PHP_CodeSniffer_Exception($error);
}

/**
 * Processes pattern strings and checks that the code conforms to the pattern.
 *
 * This test essentially checks that code is correctly formatted with whitespace.
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
abstract class PHP_CodeSniffer_Standards_AbstractPatternSniff implements PHP_CodeSniffer_Sniff
{

    /**
     * The parsed patterns array.
     *
     * @var array
     */
    private $_parsedPatterns = array();

    /**
     * Tokens that wish this sniff wishes to process outside of the patterns.
     *
     * @var array(int)
     * @see registerSupplementary()
     * @see processSupplementary()
     */
    private $_supplementaryTokens = array();

    /**
     * If true, comments will be ignored if they are found in the code.
     *
     * @var boolean
     */
    private $_ignoreComments = false;

    /**
     * Positions in the stack where errors have occured.
     *
     * @var array()
     */
    private $_errorPos = array();


    /**
     * Constructs a PHP_CodeSniffer_Standards_AbstractPatternSniff.
     *
     * @param boolean $ignoreComments If true, comments will be ignored.
     */
    public function __construct($ignoreComments=false)
    {
        $this->_ignoreComments      = $ignoreComments;
        $this->_supplementaryTokens = $this->registerSupplementary();

    }//end __construct()


    /**
     * Registers the tokens to listen to.
     *
     * Classes extending <i>AbstractPatternTest</i> should implement the
     * <i>getPatterns()</i> method to register the patterns they wish to test.
     *
     * @return array(int)
     * @see process()
     */
    public final function register()
    {
        $listenTypes = array();
        $patterns    = $this->getPatterns();

        foreach ($patterns as $pattern) {

            $parsedPattern = $this->_parse($pattern);

            // Find a token position in the pattern that we can use for a listener
            // token.
            $pos           = $this->_getListenerTokenPos($parsedPattern);
            $tokenType     = $parsedPattern[$pos]['token'];
            $listenTypes[] = $tokenType;

            $patternArray = array(
                             'listen_pos'   => $pos,
                             'pattern'      => $parsedPattern,
                             'pattern_code' => $pattern,
                            );

            if (isset($this->_parsedPatterns[$tokenType]) === false) {
                $this->_parsedPatterns[$tokenType] = array();
            }

            $this->_parsedPatterns[$tokenType][] = $patternArray;

        }//end foreach

        return array_unique(array_merge($listenTypes, $this->_supplementaryTokens));

    }//end register()


    /**
     * Returns the token types that the specified pattern is checking for.
     *
     * Returned array is in the format:
     * <code>
     *   array(
     *      T_WHITESPACE => 0, // 0 is the position where the T_WHITESPACE token
     *                         // should occur in the pattern.
     *   );
     * </code>
     *
     * @param array $pattern The parsed pattern to find the acquire the token
     *                       types from.
     *
     * @return array(int => int)
     */
    private function _getPatternTokenTypes($pattern)
    {
        $tokenTypes = array();
        foreach ($pattern as $pos => $patternInfo) {
            if ($patternInfo['type'] === 'token') {
                if (isset($tokenTypes[$patternInfo['token']]) === false) {
                    $tokenTypes[$patternInfo['token']] = $pos;
                }
            }
        }

        return $tokenTypes;

    }//end _getPatternTokenTypes()


    /**
     * Returns the position in the pattern that this test should register as
     * a listener for the pattern.
     *
     * @param array $pattern The pattern to acquire the listener for.
     *
     * @return int The postition in the pattern that this test should register
     *             as the listener.
     * @throws PHP_CodeSniffer_Exception If we could not determine a token
     *                                         to listen for.
     */
    private function _getListenerTokenPos($pattern)
    {
        $tokenTypes = $this->_getPatternTokenTypes($pattern);
        $tokenCodes = array_keys($tokenTypes);
        $token      = PHP_CodeSniffer_Tokens::getHighestWeightedToken($tokenCodes);

        // If we could not get a token.
        if ($token === false) {
            $error = 'Could not determine a token to listen for';
            throw new PHP_CodeSniffer_Exception($error);
        }

        return $tokenTypes[$token];

    }//end _getListenerTokenPos()


    /**
     * Processes the test.
     *
     * @param PHP_CodeSniffer_File $phpcsFile The PHP_CodeSniffer file where the
     *                                        token occured.
     * @param int                  $stackPtr  The postion in the tokens stack
     *                                        where the listening token type was
     *                                        found.
     *
     * @return void
     * @see register()
     */
    public final function process(PHP_CodeSniffer_File $phpcsFile, $stackPtr)
    {
        $tokens = $phpcsFile->getTokens();

        if (in_array($tokens[$stackPtr]['code'], $this->_supplementaryTokens) === true) {
            $this->processSupplementary($phpcsFile, $stackPtr);
        }

        $type = $tokens[$stackPtr]['code'];

        // If the type is not set, then it must have been a token registered
        // with registerSupplementary().
        if (isset($this->_parsedPatterns[$type]) === false) {
            return;
        }

        $allErrors = array();

        // Loop over each pattern that is listening to the current token type
        // that we are processing.
        foreach ($this->_parsedPatterns[$type] as $patternInfo) {

            // If processPattern returns false, then the pattern that we are
            // checking the code with must not be design to check that code.
            $errors = $this->processPattern($patternInfo, $phpcsFile, $stackPtr);
            if ($errors === false) {
                // The pattern didn't match.
                continue;
            } else if (empty($errors) === true) {
                // The pattern matched, but there were no errors.
                break;
            }

            foreach ($errors as $stackPtr => $error) {
                if (isset($this->_errorPos[$stackPtr]) === false) {
                    $this->_errorPos[$stackPtr] = true;
                    $allErrors[$stackPtr]       = $error;
                }
            }
        }

        foreach ($allErrors as $stackPtr => $error) {
            $phpcsFile->addError($error, $stackPtr);
        }

    }//end process()


    /**
     * Processes the pattern and verifies the code at $stackPtr.
     *
     * @param array                $patternInfo Information about the pattern used
     *                                          for checking, which includes are
     *                                          parsed token representation of the
     *                                          pattern.
     * @param PHP_CodeSniffer_File $phpcsFile   The PHP_CodeSniffer file where the
     *                                          token occured.
     * @param int                  $stackPtr    The postion in the tokens stack where
     *                                          the listening token type was found.
     *
     * @return array(errors)
     */
    protected function processPattern(
        $patternInfo,
        PHP_CodeSniffer_File $phpcsFile,
        $stackPtr
    ) {
        $tokens      = $phpcsFile->getTokens();
        $pattern     = $patternInfo['pattern'];
        $patternCode = $patternInfo['pattern_code'];
        $errors      = array();
        $found       = '';

        $ignoreTokens = array(T_WHITESPACE);

        if ($this->_ignoreComments === true) {
            $ignoreTokens
                = array_merge($ignoreTokens, PHP_CodeSniffer_Tokens::$commentTokens);
        }

        $origStackPtr = $stackPtr;
        $hasError     = false;

        if ($patternInfo['listen_pos'] > 0) {
            $stackPtr--;

            for ($i = ($patternInfo['listen_pos'] - 1); $i >= 0; $i--) {

                if ($pattern[$i]['type'] === 'token') {

                    if ($pattern[$i]['token'] === T_WHITESPACE) {

                        if ($tokens[$stackPtr]['code'] === T_WHITESPACE) {
                            $found = $tokens[$stackPtr]['content'].$found;
                        }

                        // Only check the size of the whitespace if this is not
                        // not the first token. We don't care about the size of
                        // leading whitespace, just that there is some.
                        if ($i !== 0) {
                            if ($tokens[$stackPtr]['content'] !== $pattern[$i]['value']) {
                                $hasError = true;
                            }
                        }

                    } else {

                        // Check to see if this important token is the same as the
                        // previous important token in the pattern. If it is not,
                        // then the pattern cannot be for this piece of code.
                        $prev = $phpcsFile->findPrevious(
                            $ignoreTokens,
                            $stackPtr,
                            null,
                            true
                        );

                        if ($prev === false
                            || $tokens[$prev]['code'] !== $pattern[$i]['token']
                        ) {
                            return false;
                        }

                        // If we skipped past some whitespace tokens, then add them
                        // to the found string.
                        if (($stackPtr - $prev) > 1) {
                            for ($j = ($stackPtr - 1); $j > $prev; $j--) {
                                $found = $tokens[$j]['content'].$found;
                            }
                        }

                        $found = $tokens[$prev]['content'].$found;

                        if (isset($pattern[($i - 1)]) === true
                            && $pattern[($i - 1)]['type'] === 'skip'
                        ) {
                            $stackPtr = $prev;
                        } else {
                            $stackPtr = ($prev - 1);
                        }

                    }//end if
                } else if ($pattern[$i]['type'] === 'skip') {
                    // Skip to next piece of relevant code.
                    if ($pattern[$i]['to'] === 'parenthesis_closer') {
                        $to = 'parenthesis_opener';
                    } else {
                        $to = 'scope_opener';
                    }

                    // Find the previous opener.
                    $next = $phpcsFile->findPrevious(
                        $ignoreTokens,
                        $stackPtr,
                        null,
                        true
                    );

                    if ($next === false || isset($tokens[$next][$to]) === false) {
                        // If there was not opener, then we must be
                        // using the wrong pattern.
                        return false;
                    }

                    if ($to === 'parenthesis_opener') {
                        $found = '{'.$found;
                    } else {
                        $found = '('.$found;
                    }

                    $found = '...'.$found;

                    // Skip to the opening token.
                    $stackPtr = ($tokens[$next][$to] - 1);
                } else if ($pattern[$i]['type'] === 'string') {
                    $found = 'abc';
                } else if ($pattern[$i]['type'] === 'newline') {
                    $found = 'EOL';
                }//end if
            }//end for
        }//end if

        $stackPtr          = $origStackPtr;
        $lastAddedStackPtr = null;
        $patternLen        = count($pattern);

        for ($i = $patternInfo['listen_pos']; $i < $patternLen; $i++) {
            if ($pattern[$i]['type'] === 'token') {
                if ($pattern[$i]['token'] === T_WHITESPACE) {
                    if ($this->_ignoreComments === true) {
                        // If we are ignoring comments, check to see if this current
                        // token is a comment. If so skip it.
                        if (in_array($tokens[$stackPtr]['code'], PHP_CodeSniffer_Tokens::$commentTokens) === true) {
                            continue;
                        }

                        // If the next token is a comment, the we need to skip the
                        // current token as we should allow a space before a
                        // comment for readability.
                        if (in_array($tokens[($stackPtr + 1)]['code'], PHP_CodeSniffer_Tokens::$commentTokens) === true) {
                            continue;
                        }
                    }

                    $tokenContent = '';
                    if ($tokens[$stackPtr]['code'] === T_WHITESPACE) {
                        if (isset($pattern[($i + 1)]) === false) {
                            // This is the last token in the pattern, so just compare
                            // the next token of content.
                            $tokenContent = $tokens[$stackPtr]['content'];
                        } else {
                            // Get all the whitespace to the next token.
                            $next = $phpcsFile->findNext(
                                PHP_CodeSniffer_Tokens::$emptyTokens,
                                $stackPtr,
                                null,
                                true
                            );

                            $tokenContent = $phpcsFile->getTokensAsString(
                                $stackPtr,
                                ($next - $stackPtr)
                            );

                            $lastAddedStackPtr = $stackPtr;
                            $stackPtr          = $next;
                        }

                        if ($stackPtr !== $lastAddedStackPtr) {
                            $found .= $tokenContent;
                        }
                    } else {
                        if ($stackPtr !== $lastAddedStackPtr) {
                            $found            .= $tokens[$stackPtr]['content'];
                            $lastAddedStackPtr = $stackPtr;
                        }
                    }//end if

                    if (isset($pattern[($i + 1)]) === true
                        && $pattern[($i + 1)]['type'] === 'skip'
                    ) {
                        // The next token is a skip token, so we just need to make
                        // sure the whitespace we found has *at least* the
                        // whitespace required.
                        if (strpos($tokenContent, $pattern[$i]['value']) !== 0) {
                            $hasError = true;
                        }
                    } else {
                        if ($tokenContent !== $pattern[$i]['value']) {
                            $hasError = true;
                        }
                    }

                } else {
                    // Check to see if this important token is the same as the
                    // next important token in the pattern. If it is not, then
                    // the pattern cannot be for this piece of code.
                    $next = $phpcsFile->findNext(
                        $ignoreTokens,
                        $stackPtr,
                        null,
                        true
                    );

                    if ($next === false
                        || $tokens[$next]['code'] !== $pattern[$i]['token']
                    ) {
                        return false;
                    }

                    // If we skipped past some whitespace tokens, then add them
                    // to the found string.
                    if (($next - $stackPtr) > 0) {
                        $hasComment = false;
                        for ($j = $stackPtr; $j < $next; $j++) {
                            $found .= $tokens[$j]['content'];
                            if (in_array($tokens[$j]['code'], PHP_CodeSniffer_Tokens::$commentTokens) === true) {
                                $hasComment = true;
                            }
                        }

                        // If we are not ignoring comments, this additional
                        // whitespace or comment is not allowed. If we are
                        // ignoring comments, there needs to be at least one
                        // comment for this to be allowed.
                        if ($this->_ignoreComments === false
                            || ($this->_ignoreComments === true
                            && $hasComment === false)
                        ) {
                            $hasError = true;
                        }

                        // Even when ignoring comments, we are not allowed to include
                        // newlines without the pattern specifying them, so
                        // everything should be on the same line.
                        if ($tokens[$next]['line'] !== $tokens[$stackPtr]['line']) {
                            $hasError = true;
                        }
                    }//end if

                    if ($next !== $lastAddedStackPtr) {
                        $found            .= $tokens[$next]['content'];
                        $lastAddedStackPtr = $next;
                    }

                    if (isset($pattern[($i + 1)]) === true
                        && $pattern[($i + 1)]['type'] === 'skip'
                    ) {
                        $stackPtr = $next;
                    } else {
                        $stackPtr = ($next + 1);
                    }
                }//end if

            } else if ($pattern[$i]['type'] === 'skip') {
                if ($pattern[$i]['to'] === 'unknown') {
                    $next = $phpcsFile->findNext(
                        $pattern[($i + 1)]['token'],
                        $stackPtr
                    );

                    if ($next === false) {
                        // Couldn't find the next token, sowe we must
                        // be using the wrong pattern.
                        return false;
                    }

                    $found   .= '...';
                    $stackPtr = $next;
                } else {
                    // Find the previous opener.
                    $next = $phpcsFile->findPrevious(
                        PHP_CodeSniffer_Tokens::$blockOpeners,
                        $stackPtr
                    );

                    if ($next === false
                        || isset($tokens[$next][$pattern[$i]['to']]) === false
                    ) {
                        // If there was not opener, then we must
                        // be using the wrong pattern.
                        return false;
                    }

                    $found .= '...';
                    if ($pattern[$i]['to'] === 'parenthesis_closer') {
                        $found .= ')';
                    } else {
                        $found .= '}';
                    }

                    // Skip to the closing token.
                    $stackPtr = ($tokens[$next][$pattern[$i]['to']] + 1);
                }
            } else if ($pattern[$i]['type'] === 'string') {
                if ($tokens[$stackPtr]['code'] !== T_STRING) {
                    $hasError = true;
                }

                if ($stackPtr !== $lastAddedStackPtr) {
                    $found            .= 'abc';
                    $lastAddedStackPtr = $stackPtr;
                }

                $stackPtr++;
            } else if ($pattern[$i]['type'] === 'newline') {
                // Find the next token that contains a newline character.
                $newline = 0;
                for ($j = $stackPtr; $j < $phpcsFile->numTokens; $j++) {
                    if (strpos($tokens[$j]['content'], $phpcsFile->eolChar) !== false) {
                        $newline = $j;
                        break;
                    }
                }

                if ($newline === 0) {
                    // We didn't find a newline character in the rest of the file.
                    $next     = ($phpcsFile->numTokens - 1);
                    $hasError = true;
                } else {
                    if ($this->_ignoreComments === false) {
                        // The newline character cannot be part of a comment.
                        if (in_array($tokens[$newline]['code'], PHP_CodeSniffer_Tokens::$commentTokens) === true) {
                            $hasError = true;
                        }
                    }

                    if ($newline === $stackPtr) {
                        $next = ($stackPtr + 1);
                    } else {
                        // Check that there were no significant tokens that we
                        // skipped over to find our newline character.
                        $next = $phpcsFile->findNext(
                            $ignoreTokens,
                            $stackPtr,
                            null,
                            true
                        );

                        if ($next < $newline) {
                            // We skipped a non-ignored token.
                            $hasError = true;
                        } else {
                            $next = ($newline + 1);
                        }
                    }
                }//end if

                if ($stackPtr !== $lastAddedStackPtr) {
                    $found .= $phpcsFile->getTokensAsString(
                        $stackPtr,
                        ($next - $stackPtr)
                    );

                    $diff = ($next - $stackPtr);
                    $lastAddedStackPtr = ($next - 1);
                }

                $stackPtr = $next;
            }//end if
        }//end for

        if ($hasError === true) {
            $error = $this->prepareError($found, $patternCode);
            $errors[$origStackPtr] = $error;
        }

        return $errors;

    }//end processPattern()


    /**
     * Prepares an error for the specified patternCode.
     *
     * @param string $found       The actual found string in the code.
     * @param string $patternCode The expected pattern code.
     *
     * @return string The error message.
     */
    protected function prepareError($found, $patternCode)
    {
        $found    = str_replace("\r\n", '\n', $found);
        $found    = str_replace("\n", '\n', $found);
        $found    = str_replace("\r", '\n', $found);
        $found    = str_replace('EOL', '\n', $found);
        $expected = str_replace('EOL', '\n', $patternCode);

        $error = "Expected \"$expected\"; found \"$found\"";

        return $error;

    }//end prepareError()


    /**
     * Returns the patterns that should be checked.
     *
     * @return array(string)
     */
    protected abstract function getPatterns();


    /**
     * Registers any supplementary tokens that this test might wish to process.
     *
     * A sniff may wish to register supplementary tests when it wishes to group
     * an arbitary validation that cannot be performed using a pattern, with
     * other pattern tests.
     *
     * @return array(int)
     * @see processSupplementary()
     */
    protected function registerSupplementary()
    {
        return array();

    }//end registerSupplementary()


     /**
      * Processes any tokens registered with registerSupplementary().
      *
      * @param PHP_CodeSniffer_File $phpcsFile The PHP_CodeSniffer file where to
      *                                        process the skip.
      * @param int                  $stackPtr  The position in the tokens stack to
      *                                        process.
      *
      * @return void
      * @see registerSupplementary()
      */
    protected function processSupplementary(
        PHP_CodeSniffer_File $phpcsFile,
        $stackPtr
    ) {
         return;

    }//end processSupplementary()


    /**
     * Parses a pattern string into an array of pattern steps.
     *
     * @param string $pattern The pattern to parse.
     *
     * @return array The parsed pattern array.
     * @see _createSkipPattern()
     * @see _createTokenPattern()
     */
    private function _parse($pattern)
    {
        $patterns   = array();
        $length     = strlen($pattern);
        $lastToken  = 0;
        $firstToken = 0;

        for ($i = 0; $i < $length; $i++) {

            $specialPattern = false;
            $isLastChar     = ($i === ($length - 1));
            $oldFirstToken  = $firstToken;

            if (substr($pattern, $i, 3) === '...') {
                // It's a skip pattern. The skip pattern requires the
                // content of the token in the "from" position and the token
                // to skip to.
                $specialPattern = $this->_createSkipPattern($pattern, ($i - 1));
                $lastToken      = ($i - $firstToken);
                $firstToken     = ($i + 3);
                $i              = ($i + 3);

                if ($specialPattern['to'] !== 'unknown') {
                    $firstToken++;
                }
            } else if (substr($pattern, $i, 3) === 'abc') {
                $specialPattern = array('type' => 'string');
                $lastToken      = ($i - $firstToken);
                $firstToken     = ($i + 3);
                $i              = ($i + 2);
            } else if (substr($pattern, $i, 3) === 'EOL') {
                $specialPattern = array('type' => 'newline');
                $lastToken      = ($i - $firstToken);
                $firstToken     = ($i + 3);
                $i              = ($i + 2);
            }

            if ($specialPattern !== false || $isLastChar === true) {

                // If we are at the end of the string, don't worry about a limit.
                if ($isLastChar === true) {
                    // Get the string from the end of the last skip pattern, if any,
                    // to the end of the pattern string.
                    $str = substr($pattern, $oldFirstToken);
                } else {
                    // Get the string from the end of the last special pattern,
                    // if any, to the start of this special pattern.
                    $str = substr($pattern, $oldFirstToken, $lastToken);
                }

                $tokenPatterns = $this->_createTokenPattern($str);

                // Make sure we don't skip the last token.
                if ($isLastChar === false && $i === ($length - 1)) {
                    $i--;
                }

                foreach ($tokenPatterns as $tokenPattern) {
                    $patterns[] = $tokenPattern;
                }
            }//end if

            // Add the skip pattern *after* we have processed
            // all the tokens from the end of the last skip pattern
            // to the start of this skip pattern.
            if ($specialPattern !== false) {
                $patterns[] = $specialPattern;
            }

        }//end for

        return $patterns;

    }//end _parse()


    /**
     * Creates a skip pattern.
     *
     * @param string $pattern The pattern being parsed.
     * @param string $from    The token content that the skip pattern starts from.
     *
     * @return array The pattern step.
     * @see _createTokenPattern()
     * @see _parse()
     */
    private function _createSkipPattern($pattern, $from)
    {
        $skip = array('type' => 'skip');

        $nestedParenthesis = 0;
        for ($start = $from; $start >= 0; $start--) {
            switch ($pattern[$start]) {
            case '(':
                if ($nestedParenthesis === 0) {
                    $skip['to'] = 'parenthesis_closer';
                }

                $nestedParenthesis--;
                break;
            case '{':
                $skip['to'] = 'scope_closer';
                break;
            case ')':
                $nestedParenthesis++;
                break;
            }

            if (isset($skip['to']) === true) {
                break;
            }
        }

        if (isset($skip['to']) === false) {
            $skip['to'] = 'unknown';
        }

        return $skip;

    }//end _createSkipPattern()


    /**
     * Creates a token pattern.
     *
     * @param string $str The tokens string that the pattern should match.
     *
     * @return array The pattern step.
     * @see _createSkipPattern()
     * @see _parse()
     */
    private function _createTokenPattern($str)
    {
        // Don't add a space after the closing php tag as it will add a new
        // whitespace token.
        $tokens = token_get_all('<?php '.$str.'?>');

        // Remove the <?php tag from the front and the end php tag from the back.
        $tokens = array_slice($tokens, 1, (count($tokens) - 2));

        foreach ($tokens as &$token) {
            $token = PHP_CodeSniffer::standardiseToken($token);
        }

        $patterns = array();
        foreach ($tokens as $patternInfo) {
            $patterns[] = array(
                           'type'  => 'token',
                           'token' => $patternInfo['code'],
                           'value' => $patternInfo['content'],
                          );
        }

        return $patterns;

    }//end _createTokenPattern()


}//end class

?>
