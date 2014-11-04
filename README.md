Material Dialog Backport
========================

Usage
-----

This library mimics the `AlertDialog`, so for the most part you can replace instances of
`AlertDialog` and `AlertDialog.Builder` with `MaterialDialog` and `MaterialDialog.Builder`
respectively.

Until this is launched onto Maven Central, you'll need to add add the repository in the git repo:

```
repositories {
    mavenCentral()
    maven { url "https://raw.github.com/prolificinteractive/material-dialog-backport/master/maven-repo" }
}
```

Then add the following line to your dependancies, replacing `X.X.X` with the current version:

```
compile 'com.prolificinteractive:materialdialog:X.X.X@aar'
```

Differences
-----------

There are some additions and omissions that may change your code:

1. In addition to OnClickListener, there is also OnClickDelegate, which allows you to control
   the automatic dismissal of the dialog when a button is clicked.
2. Since `Builder.setPositiveButton("OK", null)` is now ambiguous, there is a 3rd option: `Builder.setPositiveButton("OK")`
3. Builder methods for lists that use a `Cursor` are not implemented
4. Deprecated methods are not implemented, such as `AlertDialog.setButton(CharSequence, OnClickListener)`
5. Title icons are not implemented, such as `AlertDialog.setIcon(Drawable)`

Contributors
------------

Would you like to contribute? Fork us and send a pull request! Be sure to checkout our issues first.

License
-------

>Copyright 2014 Prolific Interactive
>
>Licensed under the Apache License, Version 2.0 (the "License");
>you may not use this file except in compliance with the License.
>You may obtain a copy of the License at
>
>   http://www.apache.org/licenses/LICENSE-2.0
>
>Unless required by applicable law or agreed to in writing, software
>distributed under the License is distributed on an "AS IS" BASIS,
>WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
>See the License for the specific language governing permissions and
>limitations under the License.