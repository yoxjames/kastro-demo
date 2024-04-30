# Kastro Demo

https://yoxjames.github.io/kastro-demo/

This project is a demo of my library [Kastro](https://github.com/yoxjames/Kastro). Kastro is a Kotlin Multiplatform 
library for calculating information about the moon and sun. 

Please check it out at https://github.com/yoxjames/Kastro. 

This project is an implementation of a static site generator which generates a site which generates a site that consumes
Kastro's Javascript implementation. All calculations run locally on your browser via JS as the site is entirely static.
However, the site makes heavy use of Javascript for an interactive UI despite having no server actually be required
for the interactive content. The only functionality that requires an ongoing internet connection is the leaflet map 
which may need to download new tiles to show you new areas of the map.

The goal of this is to:
1. Make it easy to find, reproduce, and report bugs in Kastro
2. Show off what Kastro can calculate.
3. Make a site that could potentially be useful.

## What this is not
In contrast to Kastro this is not something I would consider "production ready." This is a prototype tech demo and the
code reflects this in many places. There's many things I would have done differently for a more complex project or one
that I intended others to actually consume. 

If you attempt to build this locally you'll be unable to. This is due to missing artifacts. There's two:
1. dev.jamesyox:svgk
    * This is a library that implements the svg protocol in Kotlin. This is basically `kotlinx-html`
    but for the svg protocol. This is getting fairly close to done and I hope to actually publish this soon.
2. dev.jamesyox:statik
    * This is a generic static site generator. It's very early alpha. Inspiration for it came from all the problems I 
   faced making this demo.

As you might be able to guess, these are two projects I am currently working on but have not published. Some day I hope
to turn these into real libraries that could be used by others.

I plan to eventually update this demo with the published versions of those libraries so it can showcase those as well. 
However, for now this code mainly exists to show how Kastro could be used in a real application.

## How to use
When you load Kastro by default you will be looking at the current day somewhere in Denver, CO. You are free to change
the location by dragging the map or manually updating the coordinates. The date can also be changed.
If you do not see a map you may be on a smaller screen where the settings can only be activated by 
pushing the ![settings](./docs/settings.svg) button.

Let's say you wanted to always load your home location. If you just go to https://yoxjames.github.ui/kastro-demo you'll
be set to Denver, CO at the current date! That's probably not useful to you unless you happen to live in Denver, CO. You are free to change your location by dragging the map or manually entering the coordinates but that would be quite annoying. You could do that one time then click "Copy Location Link." This will produce a URL that will link the demo to a specific location. For example:

https://yoxjames.github.io/kastro-demo/?latitude=42.88364&longitude=-78.87772

Would link you directly to Buffalo, NY. 

Feel free to add a direct link to your favorites or however you prefer.

## Reporting bugs
Bug reports for both the demo and Kastro are welcome. However, I am most concerned with bugs in Kastro. If you suspect a
bug you can click the "Copy Link" button in the demo. This will generate a URL with all the params you currently have
set. This includes things like location and the date.

For example:

https://yoxjames.github.io/kastro-demo/?latitude=42.88364&longitude=-78.87772&date=2024-04-27

If clicked would set the demo to the Buffalo, NY area on April 27th, 2024. 

So simply copy the link for any bug you find and log that in your issue 
[kastro-demo](https://github.com/yoxjames/kastro-demo) project.


