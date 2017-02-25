# Holiday app Android

Application for searching holiday offers. Works with dedicated server which allows to cut off usage of mobile resources. For example, images before sending to app are scaled down to limit network data and CPU used by mobile (in process of scaling down).

## Demo
!![demo.gif](/demo-ezgif.com.gif)

## Server project

Project tied with [kkurczewski/holiday-app-server](https://github.com/kkurczewski/holiday-app-server).

## Functionalities

App functionalities:
* supports devices from API 16+
* supports mobile and tablets (separate layouts)
* handle push notification (from Firebase Cloud Messaging)
* asks server for images in specified size
* shows location on Google Map if provided

## Libraries

Used libraries:
* Android Support library
* Volley
* Firebase
* [Glide](https://github.com/bumptech/glide)
* [ExpandableTextView](https://github.com/Blogcat/Android-ExpandableTextView)
* [Betterpickers](https://github.com/code-troopers/android-betterpickers)
* [ViewPagerIndicator](https://github.com/JakeWharton/ViewPagerIndicator)

## End note

Project may not work/compile due the fact of cut off sensitive data like custom keys, private urls, etc. It's supposed to be more like read-only portfolio.
