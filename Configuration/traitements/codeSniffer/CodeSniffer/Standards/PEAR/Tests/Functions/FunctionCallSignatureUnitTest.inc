<?php

test();
test($arg, $arg2);
test ();
test( );
test() ;
test( $arg);
test( $arg );
test ( $arg );

if (is_array($arg) === true) {

}

$something = get($arg1, $arg2);
$something = get($arg1, $arg2) ;
$something = get($arg1, $arg2)   ;

// No errors as this test only checks for function calls.
class TestClass extends MyClass
{

    const const1 = 'hello';
    const CONST2 = 'hello';

    public function test () { }
}

make_foo($string/*the string*/, true/*test*/);
make_foo($string/*the string*/, true/*test*/ );
make_foo($string /*the string*/, true /*test*/);
make_foo(/*the string*/$string, /*test*/true);
make_foo( /*the string*/$string, /*test*/true);

// No errors should be throw here because
// this is not a function call per se.
throw new Exception(
    'Exception text'
);

// Or here.
$obj = new TestClass( );
?>
