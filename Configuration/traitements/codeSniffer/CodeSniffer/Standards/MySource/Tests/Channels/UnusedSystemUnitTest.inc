<?php
Channels::includeSystem('OurSystem');
Channels::includeSystem('Page');
Channels::includeAsset('Page2');
Channels::includeAsset('Folder');

function one() {
    Channels::includeSystem('MySystem');
    $siteid = MySystem2::getCurrentSiteId();
    Page::create();
    PageAssetType::create();
}

function two() {
    Page2AssetType::create();
    Folder::getFolder();
}

Channels::includeAsset('Asset');
class AssetListingAssetType extends AssetAssetType
{
    function one() {
        Channels::includeSystem('MySystem2');
        Channels::includeSystem('Page2');
        $siteid = MySystem2::getCurrentSiteId();
    }

    function two() {
        $siteid = Page2::getCurrentSiteId();
    }
}

Channels::includeSystem('Log');
Channels::includeSystem('Log4');
class MyLog implements Dog, Log extends LogMe {}

Channels::includeSystem('Log1');
class MyLog implements Dog, Log1 extends LogMe {}

class MyLog implements Log2 {}

Channels::includeSystem('Log3');
class MyLog implements Log3 {}

if (self::systemExists($systemName) === FALSE) {
    return $actions;
} else {
    // Shouldnt throw an error because we dont know the system name.
    self::includeSystem($systemName);
}

Channels::includeSystem('Widget');
Widget::includeWidget('AbstractContainer');
class MyOtherWidget extends BookWidgetType {}
?>