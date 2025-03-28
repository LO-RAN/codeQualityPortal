<?php
/**
 * Tokenizes JS code.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: JS.php 293533 2010-01-14 05:55:31Z squiz $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Tokenizes JS code.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class PHP_CodeSniffer_Tokenizers_JS
{

    /**
     * A list of tokens that are allowed to open a scope.
     *
     * This array also contains information about what kind of token the scope
     * opener uses to open and close the scope, if the token strictly requires
     * an opener, if the token can share a scope closer, and who it can be shared
     * with. An example of a token that shares a scope closer is a CASE scope.
     *
     * @var array
     */
    public $scopeOpeners = array(
                            T_IF       => array(
                                           'start'  => array(T_OPEN_CURLY_BRACKET),
                                           'end'    => array(T_CLOSE_CURLY_BRACKET),
                                           'strict' => false,
                                           'shared' => false,
                                           'with'   => array(),
                                          ),
                            T_TRY      => array(
                                           'start'  => array(T_OPEN_CURLY_BRACKET),
                                           'end'    => array(T_CLOSE_CURLY_BRACKET),
                                           'strict' => true,
                                           'shared' => false,
                                           'with'   => array(),
                                          ),
                            T_CATCH    => array(
                                           'start'  => array(T_OPEN_CURLY_BRACKET),
                                           'end'    => array(T_CLOSE_CURLY_BRACKET),
                                           'strict' => true,
                                           'shared' => false,
                                           'with'   => array(),
                                          ),
                            T_ELSE     => array(
                                           'start'  => array(T_OPEN_CURLY_BRACKET),
                                           'end'    => array(T_CLOSE_CURLY_BRACKET),
                                           'strict' => false,
                                           'shared' => false,
                                           'with'   => array(),
                                          ),
                            T_FOR      => array(
                                           'start'  => array(T_OPEN_CURLY_BRACKET),
                                           'end'    => array(T_CLOSE_CURLY_BRACKET),
                                           'strict' => false,
                                           'shared' => false,
                                           'with'   => array(),
                                          ),
                            T_FUNCTION => array(
                                           'start'  => array(T_OPEN_CURLY_BRACKET),
                                           'end'    => array(T_CLOSE_CURLY_BRACKET),
                                           'strict' => false,
                                           'shared' => false,
                                           'with'   => array(),
                                          ),
                            T_WHILE    => array(
                                           'start'  => array(T_OPEN_CURLY_BRACKET),
                                           'end'    => array(T_CLOSE_CURLY_BRACKET),
                                           'strict' => false,
                                           'shared' => false,
                                           'with'   => array(),
                                          ),
                            T_DO       => array(
                                           'start'  => array(T_OPEN_CURLY_BRACKET),
                                           'end'    => array(T_CLOSE_CURLY_BRACKET),
                                           'strict' => true,
                                           'shared' => false,
                                           'with'   => array(),
                                          ),
                            T_SWITCH   => array(
                                           'start'  => array(T_OPEN_CURLY_BRACKET),
                                           'end'    => array(T_CLOSE_CURLY_BRACKET),
                                           'strict' => true,
                                           'shared' => false,
                                           'with'   => array(),
                                          ),
                            T_CASE     => array(
                                           'start'  => array(T_COLON),
                                           'end'    => array(T_BREAK),
                                           'strict' => true,
                                           'shared' => true,
                                           'with'   => array(
                                                        T_DEFAULT,
                                                        T_CASE,
                                                        T_SWITCH,
                                                       ),
                                          ),
                            T_DEFAULT  => array(
                                           'start'  => array(T_COLON),
                                           'end'    => array(T_BREAK),
                                           'strict' => true,
                                           'shared' => true,
                                           'with'   => array(
                                                        T_CASE,
                                                        T_SWITCH,
                                                       ),
                                          ),
                           );

    /**
     * A list of tokens that end the scope.
     *
     * This array is just a unique collection of the end tokens
     * from the _scopeOpeners array. The data is duplicated here to
     * save time during parsing of the file.
     *
     * @var array
     */
    public $endScopeTokens = array(
                              T_CLOSE_CURLY_BRACKET,
                              T_BREAK,
                             );

    /**
     * A list of special JS tokens and their types.
     *
     * @var array
     */
    protected $tokenValues = array(
                              'function'  => 'T_FUNCTION',
                              'prototype' => 'T_PROTOTYPE',
                              'try'       => 'T_TRY',
                              'catch'     => 'T_CATCH',
                              'return'    => 'T_RETURN',
                              'break'     => 'T_BREAK',
                              'switch'    => 'T_SWITCH',
                              'continue'  => 'T_CONTINUE',
                              'if'        => 'T_IF',
                              'else'      => 'T_ELSE',
                              'do'        => 'T_DO',
                              'while'     => 'T_WHILE',
                              'for'       => 'T_FOR',
                              'var'       => 'T_VAR',
                              'case'      => 'T_CASE',
                              'default'   => 'T_DEFAULT',
                              'true'      => 'T_TRUE',
                              'false'     => 'T_FALSE',
                              'null'      => 'T_NULL',
                              'this'      => 'T_THIS',
                              '('         => 'T_OPEN_PARENTHESIS',
                              ')'         => 'T_CLOSE_PARENTHESIS',
                              '{'         => 'T_OPEN_CURLY_BRACKET',
                              '}'         => 'T_CLOSE_CURLY_BRACKET',
                              '['         => 'T_OPEN_SQUARE_BRACKET',
                              ']'         => 'T_CLOSE_SQUARE_BRACKET',
                              '?'         => 'T_INLINE_THEN',
                              '.'         => 'T_OBJECT_OPERATOR',
                              '+'         => 'T_PLUS',
                              '-'         => 'T_MINUS',
                              '*'         => 'T_MULTIPLY',
                              '%'         => 'T_MODULUS',
                              '/'         => 'T_DIVIDE',
                              ','         => 'T_COMMA',
                              ';'         => 'T_SEMICOLON',
                              ':'         => 'T_COLON',
                              '<'         => 'T_LESS_THAN',
                              '>'         => 'T_GREATER_THAN',
                              '<='        => 'T_IS_SMALLER_OR_EQUAL',
                              '>='        => 'T_IS_GREATER_OR_EQUAL',
                              '!'         => 'T_BOOLEAN_NOT',
                              '||'        => 'T_BOOLEAN_OR',
                              '&&'        => 'T_BOOLEAN_AND',
                              '|'         => 'T_BITWISE_OR',
                              '&'         => 'T_BITWISE_AND',
                              '!='        => 'T_IS_NOT_EQUAL',
                              '!=='       => 'T_IS_NOT_IDENTICAL',
                              '='         => 'T_EQUAL',
                              '=='        => 'T_IS_EQUAL',
                              '==='       => 'T_IS_IDENTICAL',
                              '-='        => 'T_MINUS_EQUAL',
                              '+='        => 'T_PLUS_EQUAL',
                              '*='        => 'T_MUL_EQUAL',
                              '/='        => 'T_DIV_EQUAL',
                              '++'        => 'T_INC',
                              '--'        => 'T_DEC',
                              '//'        => 'T_COMMENT',
                              '/*'        => 'T_COMMENT',
                              '/**'       => 'T_DOC_COMMENT',
                              '*/'        => 'T_COMMENT',
                             );

    /**
     * A list string delimiters.
     *
     * @var array
     */
    protected $stringTokens = array(
                               '\'',
                               '"',
                              );

    /**
     * A list tokens that start and end comments.
     *
     * @var array
     */
    protected $commentTokens = array(
                                '//'  => null,
                                '/*'  => '*/',
                                '/**' => '*/',
                               );


    /**
     * Creates an array of tokens when given some PHP code.
     *
     * Starts by using token_get_all() but does a lot of extra processing
     * to insert information about the context of the token.
     *
     * @param string $string  The string to tokenize.
     * @param string $eolChar The EOL character to use for splitting strings.
     *
     * @return array
     */
    public function tokenizeString($string, $eolChar='\n')
    {
        $tokenTypes = array_keys($this->tokenValues);

        $maxTokenLength = 0;
        foreach ($tokenTypes as $token) {
            if (strlen($token) > $maxTokenLength) {
                $maxTokenLength = strlen($token);
            }
        }

        $tokens          = array();
        $inString        = '';
        $stringChar      = null;
        $inComment       = '';
        $buffer          = '';
        $preStringBuffer = '';
        $cleanBuffer     = false;

        $tokens[] = array(
                     'code'    => T_OPEN_TAG,
                     'type'    => 'T_OPEN_TAG',
                     'content' => '',
                    );

        if (PHP_CODESNIFFER_VERBOSITY > 1) {
            echo "\t*** START TOKENIZING ***".PHP_EOL;
        }

        // Convert newlines to single characters for ease of
        // processing. We will change them back later.
        $string = str_replace($eolChar, "\n", $string);

        $chars    = str_split($string);
        $numChars = count($chars);
        for ($i = 0; $i < $numChars; $i++) {
            $char = $chars[$i];

            if (PHP_CODESNIFFER_VERBOSITY > 1) {
                $content = str_replace("\n", '\n', $char);
                $bufferContent = str_replace("\n", '\n', $buffer);
                if ($inString !== '') {
                    echo "\t";
                }

                if ($inComment !== '') {
                    echo "\t";
                }

                echo "Process char $i => $content (buffer: $bufferContent)".PHP_EOL;
            }

            if ($inString === '' && $inComment === '' && $buffer !== '') {
                // If the buffer only has whitespace and we are about to
                // add a character, store the whitespace first.
                if (trim($char) !== '' && trim($buffer) === '') {
                    $tokens[] = array(
                                 'code'    => T_WHITESPACE,
                                 'type'    => 'T_WHITESPACE',
                                 'content' => str_replace("\n", $eolChar, $buffer),
                                );

                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        $content = str_replace("\n", '\n', $buffer);
                        echo "=> Added token T_WHITESPACE ($content)".PHP_EOL;
                    }

                    $buffer = '';
                }

                // If the buffer is not whitespace and we are about to
                // add a whitespace character, store the content first.
                if ($inString === ''
                    && $inComment === ''
                    && trim($char) === ''
                    && trim($buffer) !== ''
                ) {
                    $tokens[] = array(
                                 'code'    => T_STRING,
                                 'type'    => 'T_STRING',
                                 'content' => str_replace("\n", $eolChar, $buffer),
                                );

                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        $content = str_replace("\n", '\n', $buffer);
                        echo "=> Added token T_STRING ($content)".PHP_EOL;
                    }

                    $buffer = '';
                }
            }//end if

            // Process strings.
            if ($inComment === '' && in_array($char, $this->stringTokens) === true) {
                if ($inString === $char) {
                    // This could be the end of the string, but make sure it
                    // is not escaped first.
                    $escapes = 0;
                    for ($x = ($i - 1); $x >= 0; $x--) {
                        if ($chars[$x] !== '\\') {
                            break;
                        }

                        $escapes++;
                    }

                    if ($escapes === 0 || ($escapes % 2) === 0) {
                        // There is an even number escape chars,
                        // so this is not escaped, it is the end of the string.
                        $tokens[] = array(
                                     'code'    => T_CONSTANT_ENCAPSED_STRING,
                                     'type'    => 'T_CONSTANT_ENCAPSED_STRING',
                                     'content' => str_replace("\n", $eolChar, $buffer).$char,
                                    );

                        if (PHP_CODESNIFFER_VERBOSITY > 1) {
                            echo "\t* found end of string *".PHP_EOL;
                            $content = str_replace("\n", '\n', $buffer.$char);
                            echo "=> Added token T_CONSTANT_ENCAPSED_STRING ($content)".PHP_EOL;
                        }

                        $buffer          = '';
                        $preStringBuffer = '';
                        $inString        = '';
                        $stringChar      = null;
                        continue;
                    }
                } else if ($inString === '') {
                    $inString        = $char;
                    $stringChar      = $i;
                    $preStringBuffer = $buffer;

                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        echo "\t* looking for string closer *".PHP_EOL;
                    }
                }//end if
            }//end if

            if ($inString !== '' && $char === "\n") {
                // Unless this newline character is escaped, the string did not
                // end before the end of the line, which means it probably
                // wasn't a string at all (maybe a regex).
                if ($chars[($i - 1)] !== '\\') {
                    $i               = $stringChar;
                    $buffer          = $preStringBuffer;
                    $preStringBuffer = '';
                    $inString        = '';
                    $stringChar      = null;
                    $char            = $chars[$i];

                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        echo "\t* found newline before end of string, bailing *".PHP_EOL;
                    }
                }
            }

            $buffer .= $char;

            // We don't look for special tokens inside strings,
            // so if we are in a string, we can continue here now
            // that the current char is in the buffer.
            if ($inString !== '') {
                continue;
            }

            // Special case for T_DIVIDE which can actually be
            // the start of a regular expression.
            if ($char === '/') {
                $regex = $this->getRegexToken(
                    $i,
                    $string,
                    $chars,
                    $tokens,
                    $eolChar
                );

                if ($regex !== null) {
                    $tokens[] = array(
                                 'code'    => T_REGULAR_EXPRESSION,
                                 'type'    => 'T_REGULAR_EXPRESSION',
                                 'content' => $regex['content'],
                                );

                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        $content = str_replace("\n", '\n', $regex['content']);
                        echo '=> Added token T_REGULAR_EXPRESSION ('.$regex['content'].')'.PHP_EOL;
                    }

                    $i = $regex['end'];
                    $buffer = '';
                    $cleanBuffer = false;
                    continue;
                }
            }//end if

            // Check for known tokens, but ignore tokens found that are not at
            // the end of a string, like FOR and this.FORmat.
            if (in_array(strtolower($buffer), $tokenTypes) === true
                && (preg_match('|[a-zA-z0-9_]|', $char) === 0
                || preg_match('|[a-zA-z0-9_]|', $chars[($i + 1)]) === 0)
            ) {
                $matchedToken    = false;
                $lookAheadLength = ($maxTokenLength - strlen($buffer));

                if ($lookAheadLength > 0) {
                    // The buffer contains a token type, but we need
                    // to look ahead at the next chars to see if this is
                    // actually part of a larger token. For example,
                    // FOR and FOREACH.
                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        echo "\t* buffer possibly contains token, looking ahead $lookAheadLength chars *".PHP_EOL;
                    }

                    $charBuffer = $buffer;
                    for ($x = 1; $x <= $lookAheadLength; $x++) {
                        if (isset($chars[($i + $x)]) === false) {
                            break;
                        }

                        $charBuffer .= $chars[($i + $x)];

                        if (PHP_CODESNIFFER_VERBOSITY > 1) {
                            $content = str_replace("\n", '\n', $charBuffer);
                            echo "\t=> Looking ahead $x chars => $content".PHP_EOL;
                        }

                        if (in_array(strtolower($charBuffer), $tokenTypes) === true) {
                            // We've found something larger that matches
                            // so we can ignore this char.
                            if (PHP_CODESNIFFER_VERBOSITY > 1) {
                                $type = $this->tokenValues[strtolower($charBuffer)];
                                echo "\t* look ahead found more specific token ($type), ignoring $i *".PHP_EOL;
                            }

                            $matchedToken = true;
                            break;
                        }
                    }//end for
                }//end if

                if ($matchedToken === false) {
                    if (PHP_CODESNIFFER_VERBOSITY > 1 && $lookAheadLength > 0) {
                        echo "\t* look ahead found nothing *".PHP_EOL;
                    }

                    $value    = $this->tokenValues[strtolower($buffer)];
                    $tokens[] = array(
                                 'code'    => constant($value),
                                 'type'    => $value,
                                 'content' => $buffer,
                                );

                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        $content = str_replace("\n", '\n', $buffer);
                        echo "=> Added token $value ($content)".PHP_EOL;
                    }

                    $cleanBuffer = true;
                }//end if
            } else if (in_array(strtolower($char), $tokenTypes) === true) {
                // No matter what token we end up using, we don't
                // need the content in the buffer any more because we have
                // found a valid token.
                $newContent = substr(str_replace("\n", $eolChar, $buffer), 0, -1);
                if ($newContent !== '') {
                    $tokens[] = array(
                                 'code'    => T_STRING,
                                 'type'    => 'T_STRING',
                                 'content' => $newContent,
                                );

                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        $content = str_replace("\n", '\n', substr($buffer, 0, -1));
                        echo "=> Added token T_STRING ($content)".PHP_EOL;
                    }
                }

                if (PHP_CODESNIFFER_VERBOSITY > 1) {
                    echo "\t* char is token, looking ahead ".($maxTokenLength - 1).' chars *'.PHP_EOL;
                }

                // The char is a token type, but we need to look ahead at the
                // next chars to see if this is actually part of a larger token.
                // For example, = and ===.
                $charBuffer   = $char;
                $matchedToken = false;
                for ($x = 1; $x <= $maxTokenLength; $x++) {
                    if (isset($chars[($i + $x)]) === false) {
                        break;
                    }

                    $charBuffer .= $chars[($i + $x)];

                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        $content = str_replace("\n", '\n', $charBuffer);
                        echo "\t=> Looking ahead $x chars => $content".PHP_EOL;
                    }

                    if (in_array(strtolower($charBuffer), $tokenTypes) === true) {
                        // We've found something larger that matches
                        // so we can ignore this char.
                        if (PHP_CODESNIFFER_VERBOSITY > 1) {
                            $type = $this->tokenValues[strtolower($charBuffer)];
                            echo "\t* look ahead found more specific token ($type), ignoring $i *".PHP_EOL;
                        }

                        $matchedToken = true;
                        break;
                    }
                }//end for

                if ($matchedToken === false) {
                    $value    = $this->tokenValues[strtolower($char)];
                    $tokens[] = array(
                                 'code'    => constant($value),
                                 'type'    => $value,
                                 'content' => $char,
                                );

                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        echo "\t* look ahead found nothing *".PHP_EOL;
                        $content = str_replace("\n", '\n', $char);
                        echo "=> Added token $value ($content)".PHP_EOL;
                    }

                    $cleanBuffer = true;
                } else {
                    $buffer = $char;
                }
            }//end if

            // Keep track of content inside comments.
            if ($inComment === ''
                && array_key_exists($buffer, $this->commentTokens) === true
            ) {
                // This is not really a comment if the content
                // looks like \// (i.e., it is escaped).
                if (isset($chars[($i - 2)]) === true && $chars[($i - 2)] === '\\') {
                    $lastToken   = array_pop($tokens);
                    $lastContent = $lastToken['content'];
                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        $value   = $this->tokenValues[strtolower($lastContent)];
                        $content = str_replace("\n", '\n', $lastContent);
                        echo "=> Removed token $value ($content)".PHP_EOL;
                    }

                    $lastChars    = str_split($lastContent);
                    $lastNumChars = count($lastChars);
                    for ($x = 0; $x < $lastNumChars; $x++) {
                        $lastChar = $lastChars[$x];
                        $value    = $this->tokenValues[strtolower($lastChar)];
                        $tokens[] = array(
                                     'code'    => constant($value),
                                     'type'    => $value,
                                     'content' => $lastChar,
                                    );

                        if (PHP_CODESNIFFER_VERBOSITY > 1) {
                            $content = str_replace("\n", '\n', $lastChar);
                            echo "=> Added token $value ($content)".PHP_EOL;
                        }
                    }
                } else {
                    // We have started a comment.
                    $inComment = $buffer;

                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        echo "\t* looking for end of comment *".PHP_EOL;
                    }
                }
            } else if ($inComment !== '') {
                if ($this->commentTokens[$inComment] === null) {
                    // Comment ends at the next newline.
                    if (strpos($buffer, "\n") !== false) {
                        $inComment = '';
                    }
                } else {
                    if ($this->commentTokens[$inComment] === $buffer) {
                        $inComment = '';
                    }
                }

                if (PHP_CODESNIFFER_VERBOSITY > 1) {
                    if ($inComment === '') {
                        echo "\t* found end of comment *".PHP_EOL;
                    }
                }

                if ($inComment === '' && $cleanBuffer === false) {
                    $tokens[] = array(
                                 'code'    => T_STRING,
                                 'type'    => 'T_STRING',
                                 'content' => str_replace("\n", $eolChar, $buffer),
                                );

                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        $content = str_replace("\n", '\n', $buffer);
                        echo "=> Added token T_STRING ($content)".PHP_EOL;
                    }

                    $buffer = '';
                }
            }//end if

            if ($cleanBuffer === true) {
                $buffer      = '';
                $cleanBuffer = false;
            }
        }//end foreach

        // Trim the last newline off the end of the buffer before
        // adding it's contents to the token stack.
        // This is so we don't count the very final newline of a file.
        $buffer = substr($buffer, 0, -1);

        if (empty($buffer) === false) {
            // Buffer contians whitespace from the end of the file, and not
            // just the final newline.
            $tokens[] = array(
                         'code'    => T_WHITESPACE,
                         'type'    => 'T_WHITESPACE',
                         'content' => str_replace("\n", $eolChar, $buffer),
                        );

            if (PHP_CODESNIFFER_VERBOSITY > 1) {
                $content = str_replace($eolChar, '\n', $buffer);
                echo "=> Added token T_WHITESPACE ($content)".PHP_EOL;
            }
        }

        $tokens[] = array(
                     'code'    => T_CLOSE_TAG,
                     'type'    => 'T_CLOSE_TAG',
                     'content' => '',
                    );

        /*
            Now that we have done some basic tokenizing, we need to
            modify the tokens to join some together and split some apart
            so they match what the PHP tokenizer does.
        */

        $finalTokens = array();
        $newStackPtr = 0;
        $numTokens   = count($tokens);
        for ($stackPtr = 0; $stackPtr < $numTokens; $stackPtr++) {
            $token = $tokens[$stackPtr];

            /*
                Look for comments and join the tokens together.
            */

            if (array_key_exists($token['content'], $this->commentTokens) === true) {
                $newContent   = '';
                $tokenContent = $token['content'];
                $endContent   = $this->commentTokens[$tokenContent];
                while ($tokenContent !== $endContent) {
                    if ($endContent === null
                        && strpos($tokenContent, $eolChar) !== false
                    ) {
                        // A null end token means the comment ends at the end of
                        // the line so we look for newlines and split the token.
                        $tokens[$stackPtr]['content'] = substr(
                            $tokenContent,
                            (strpos($tokenContent, $eolChar) + strlen($eolChar))
                        );

                        $tokenContent = substr(
                            $tokenContent,
                            0,
                            (strpos($tokenContent, $eolChar) + strlen($eolChar))
                        );

                        // If the substr failed, skip the token as the content
                        // will now be blank.
                        if ($tokens[$stackPtr]['content'] !== false) {
                            $stackPtr--;
                        }

                        break;
                    }//end if

                    $stackPtr++;
                    $newContent  .= $tokenContent;
                    if (isset($tokens[$stackPtr]) === false) {
                        break;
                    }

                    $tokenContent = $tokens[$stackPtr]['content'];
                }//end while

                // Save the new content in the current token so
                // the code below can chop it up on newlines.
                $token['content'] = $newContent.$tokenContent;
            }//end if

            /*
                If this token has newlines in its content, split each line up
                and create a new token for each line. We do this so it's easier
                to asertain where errors occur on a line.
                Note that $token[1] is the token's content.
            */

            if (strpos($token['content'], $eolChar) !== false) {
                $tokenLines = explode($eolChar, $token['content']);
                $numLines   = count($tokenLines);

                for ($i = 0; $i < $numLines; $i++) {
                    $newToken['content'] = $tokenLines[$i];
                    if ($i === ($numLines - 1)) {
                        if ($tokenLines[$i] === '') {
                            break;
                        }
                    } else {
                        $newToken['content'] .= $eolChar;
                    }

                    $newToken['type']          = $token['type'];
                    $newToken['code']          = $token['code'];
                    $finalTokens[$newStackPtr] = $newToken;
                    $newStackPtr++;
                }
            } else {
                $finalTokens[$newStackPtr] = $token;
                $newStackPtr++;
            }//end if

            // Convert numbers, including decimals.
            if ($token['code'] === T_STRING
                || $token['code'] === T_OBJECT_OPERATOR
            ) {
                $newContent  = '';
                $oldStackPtr = $stackPtr;
                while (preg_match('|^[0-9\.]+$|', $tokens[$stackPtr]['content']) !== 0) {
                    $newContent .= $tokens[$stackPtr]['content'];
                    $stackPtr++;
                }

                if ($newContent !== '' && $newContent !== '.') {
                    $finalTokens[($newStackPtr - 1)]['content'] = $newContent;
                    if (ctype_digit($newContent) === true) {
                        $finalTokens[($newStackPtr - 1)]['code']
                            = constant('T_LNUMBER');
                        $finalTokens[($newStackPtr - 1)]['type'] = 'T_LNUMBER';
                    } else {
                        $finalTokens[($newStackPtr - 1)]['code']
                            = constant('T_DNUMBER');
                        $finalTokens[($newStackPtr - 1)]['type'] = 'T_DNUMBER';
                    }

                    $stackPtr--;
                } else {
                    $stackPtr = $oldStackPtr;
                }
            }//end if
        }//end for

        if (PHP_CODESNIFFER_VERBOSITY > 1) {
            echo "\t*** END TOKENIZING ***".PHP_EOL;
        }

        return $finalTokens;

    }//end tokenizeString()


    /**
     * Tokenizes a regular expression if one is found.
     *
     * If a regular expression is not found, NULL is returned.
     *
     * @param string $char    The index of the possible regex start character.
     * @param string $string  The complete content of the string being tokenized.
     * @param string $chars   An array of characters being tokenized.
     * @param string $tokens  The current array of tokens found in the string.
     * @param string $eolChar The EOL character to use for splitting strings.
     *
     * @return void
     */
    public function getRegexToken($char, $string, $chars, $tokens, $eolChar)
    {
        $beforeTokens = array(
                         T_EQUAL,
                         T_OPEN_PARENTHESIS,
                         T_RETURN,
                         T_BOOLEAN_OR,
                         T_BOOLEAN_AND,
                         T_BITWISE_OR,
                         T_BITWISE_AND,
                         T_COMMA,
                        );

        $afterTokens = array(
                         ',',
                         ')',
                         ';',
                         ' ',
                         '.',
                        );

        // Find the last non-whitespace token that was added
        // to the tokens array.
        $numTokens = count($tokens);
        for ($prev = ($numTokens - 1); $prev >= 0; $prev--) {
            if (in_array($tokens[$prev]['code'], PHP_CodeSniffer_Tokens::$emptyTokens) === false) {
                break;
            }
        }

        if (in_array($tokens[$prev]['code'], $beforeTokens) === false) {
            return null;
        }

        // This is probably a regular expression, so look for the end of it.
        if (PHP_CODESNIFFER_VERBOSITY > 1) {
            $content = str_replace("\n", '\n', $char);
            echo "\t* token possibly starts a regular expression *".PHP_EOL;
        }

        $numChars = count($chars);
        for ($next = ($char + 1); $next < $numChars; $next++) {
            if ($chars[$next] === '/') {
                // Just make sure this is not escaped first.
                if ($chars[($next - 1)] !== '\\') {
                    // In the simple form: /.../ so we found the end.
                    break;
                } else if ($chars[($next - 2)] === '\\') {
                    // In the form: /...\\/ so we found the end.
                    break;
                }
            } else {
                $possiblEolChar = substr($string, $next, strlen($eolChar));
                if ($possiblEolChar === $eolChar) {
                    // This is the last token on the line and regular
                    // expressions need to be defined on a single line,
                    // so this is not a regular expression.
                    break;
                }
            }
        }

        if ($chars[$next] !== '/') {
            return null;
        }

        while (preg_match('|[a-zA-Z]|', $chars[($next + 1)]) !== 0) {
            // The token directly after the end of the regex can
            // be modifiers like global and case insensitive
            // (.e.g, /pattern/gi).
            $next++;
        }

        $regexEnd = $next;

        for ($next = ($next + 1); $next < $numChars; $next++) {
            if ($chars[$next] !== ' ') {
                break;
            } else {
                $possiblEolChar = substr($string, $next, strlen($eolChar));
                if ($possiblEolChar === $eolChar) {
                    // This is the last token on the line.
                    break;
                }
            }
        }

        if (in_array($chars[$next], $afterTokens) === false) {
            return null;
        }

        // This is a regular expression, so join all the tokens together.
        $content = '';
        for ($x = $char; $x <= $regexEnd; $x++) {
            $content .= $chars[$x];
        }

        $token = array(
                  'start'   => $char,
                  'end'     => $regexEnd,
                  'content' => $content,
                 );

        return $token;

    }//end getRegexToken()


    /**
     * Performs additional processing after main tokenizing.
     *
     * This additional processing looks for properties, labels and objects.
     *
     * @param array  &$tokens The array of tokens to process.
     * @param string $eolChar The EOL character to use for splitting strings.
     *
     * @return void
     */
    public function processAdditional(&$tokens, $eolChar)
    {
        if (PHP_CODESNIFFER_VERBOSITY > 1) {
            echo "\t*** START ADDITIONAL JS PROCESSING ***".PHP_EOL;
        }

        $numTokens  = count($tokens);
        $classStack = array();

        for ($i = 0; $i < $numTokens; $i++) {
            if (PHP_CODESNIFFER_VERBOSITY > 1) {
                $type    = $tokens[$i]['type'];
                $content = str_replace($eolChar, '\n', $tokens[$i]['content']);
                echo str_repeat("\t", count($classStack));

                echo "\tProcess token $i: $type => $content".PHP_EOL;
            }

            if ($tokens[$i]['code'] === T_OPEN_CURLY_BRACKET
                && isset($tokens[$i]['scope_condition']) === false
            ) {
                $classStack[] = $i;
                if (PHP_CODESNIFFER_VERBOSITY > 1) {
                    echo str_repeat("\t", count($classStack));
                    echo "\t=> Found property opener".PHP_EOL;
                }

                // This could also be an object definition.
                for ($x = ($i - 1); $x >= 0; $x--) {
                    if (in_array($tokens[$x]['code'], PHP_CodeSniffer_Tokens::$emptyTokens) === false) {
                        // Non-whitespace content.
                        break;
                    }
                }

                if ($tokens[$x]['code'] === T_EQUAL) {
                    for ($x--; $x >= 0; $x--) {
                        if (in_array($tokens[$x]['code'], PHP_CodeSniffer_Tokens::$emptyTokens) === false) {
                            break;
                        }
                    }

                    if ($tokens[$x]['code'] === T_STRING
                        || $tokens[$x]['code'] === T_PROTOTYPE
                    ) {
                        // Find the first string in this definition.
                        // E.g., WantedString.DontWantThis.prototype
                        for ($x--; $x >= 0; $x--) {
                            $wantedTokens = array(
                                             T_STRING,
                                             T_PROTOTYPE,
                                             T_OBJECT_OPERATOR,
                                            );

                            if (in_array($tokens[$x]['code'], $wantedTokens) === false) {
                                $x++;
                                break;
                            }
                        }

                        $closer = $tokens[$i]['bracket_closer'];
                        $tokens[$i]['scope_condition']      = $x;
                        $tokens[$i]['scope_closer']         = $closer;
                        $tokens[$closer]['scope_condition'] = $x;
                        $tokens[$closer]['scope_opener']    = $i;
                        $tokens[$x]['scope_opener']         = $i;
                        $tokens[$x]['scope_closer']         = $closer;
                        $tokens[$x]['code']                 = T_OBJECT;
                        $tokens[$x]['type']                 = 'T_OBJECT';

                        if (PHP_CODESNIFFER_VERBOSITY > 1) {
                            echo str_repeat("\t", count($classStack));
                            echo "\t* token $x converted from T_STRING to T_OBJECT *".PHP_EOL;
                            echo str_repeat("\t", count($classStack));
                            echo "\t* set scope opener ($i) and closer ($closer) for token $x *".PHP_EOL;
                        }
                    }//end if
                }//end if
            } else if ($tokens[$i]['code'] === T_CLOSE_CURLY_BRACKET
                && (isset($tokens[$i]['scope_condition']) === false
                || $tokens[$tokens[$i]['scope_condition']]['code'] === T_OBJECT)
            ) {
                $opener = array_pop($classStack);

                if (PHP_CODESNIFFER_VERBOSITY > 1) {
                    echo str_repeat("\t", count($classStack));
                    echo "\t\t=> Found property closer for $opener".PHP_EOL;
                }
            } else if ($tokens[$i]['code'] === T_COLON) {
                // If it is a scope opener, it belongs to a
                // DEFAULT or CASE statement.
                if (isset($tokens[$i]['scope_condition']) === true) {
                    continue;
                }

                // Make sure this is not part of an inline IF statement.
                for ($x = ($i - 1); $x >= 0; $x--) {
                    if ($tokens[$x]['code'] === T_INLINE_THEN) {
                        continue(2);
                    } else if ($tokens[$x]['line'] < $tokens[$i]['line']) {
                        break;
                    }
                }

                // The string to the left of the colon is either a property or label.
                for ($label = ($i - 1); $label >= 0; $label--) {
                    if (in_array($tokens[$label]['code'], PHP_CodeSniffer_Tokens::$emptyTokens) === false) {
                        break;
                    }
                }

                if ($tokens[$label]['code'] !== T_STRING) {
                    continue;
                }

                if (empty($classStack) === false) {
                    $tokens[$label]['code'] = T_PROPERTY;
                    $tokens[$label]['type'] = 'T_PROPERTY';

                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        echo str_repeat("\t", count($classStack));
                        echo "\t* token $label converted from T_STRING to T_PROPERTY *".PHP_EOL;
                    }

                    // If the net token after the colon is a curly brace,
                    // this property is actually an object, so we can give it
                    // and opener and closer.
                    for ($x = ($i + 1); $x < $numTokens; $x++) {
                        if (in_array($tokens[$x]['code'], PHP_CodeSniffer_Tokens::$emptyTokens) === false) {
                            break;
                        }
                    }

                    if ($tokens[$x]['code'] === T_OPEN_CURLY_BRACKET) {
                        $closer = $tokens[$x]['bracket_closer'];
                        $tokens[$label]['scope_opener']     = $x;
                        $tokens[$label]['scope_closer']     = $closer;
                        $tokens[$x]['scope_condition']      = $label;
                        $tokens[$x]['scope_closer']         = $closer;
                        $tokens[$closer]['scope_condition'] = $label;
                        $tokens[$closer]['scope_opener']    = $x;
                        if (PHP_CODESNIFFER_VERBOSITY > 1) {
                            echo str_repeat("\t", count($classStack));
                            echo "\t* set scope opener ($x) and closer ($closer) for token $label *".PHP_EOL;
                        }
                    }
                } else {
                    $tokens[$label]['code'] = T_LABEL;
                    $tokens[$label]['type'] = 'T_LABEL';

                    if (PHP_CODESNIFFER_VERBOSITY > 1) {
                        echo str_repeat("\t", count($classStack));
                        echo "\t* token $label converted from T_STRING to T_LABEL *".PHP_EOL;
                    }
                }
            }//end if
        }//end for

        if (PHP_CODESNIFFER_VERBOSITY > 1) {
            echo "\t*** END ADDITIONAL JS PROCESSING ***".PHP_EOL;
        }

    }//end processAdditional()


}//end class

?>
