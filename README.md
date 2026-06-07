# Kastro Demo

https://yoxjames.github.io/kastro-demo/

This project is a demo of my library [Kastro](https://github.com/yoxjames/Kastro). Kastro is a Kotlin Multiplatform 
library for calculating information about the moon and sun. 

Kastro is an implementation of a static site generator which generates a site that consumes Kastro's Javascript implementation. All calculations run locally on your browser via JS as the site is entirely static.
However, the site makes heavy use of Javascript for an interactive UI despite having no server actually be required
for the interactive content. The only functionality that requires an ongoing internet connection is the leaflet map 
which may need to download new tiles to show you new areas of the map.

The goal of this is to:
1. Show off what Kastro can calculate.
2. Demonstrate a way of seeing sun and moon calculations in a ways that makes sense
3. Make it easy to find, reproduce, and report bugs in Kastro
4. Show off my other library SVG For Kotlin (svg4kt) https://github.com/yoxjames/svg4kt
   * This was used to render all the SVG based visuals in KotlinJS! Give it a try for your own projects!

## What this is not
In contrast to Kastro, this is not "production ready." This is a prototype tech demo and the
code reflects this in many places. There are many things I would have done differently for a more complex project or one
that I intended others to actually consume. 

You won't be able to build this locally because it's missing an artifact: 
1. dev.jamesyox:statik
    * This is a generic static site generator that's in very early alpha. The inspiration for it came from all the problems I 
   faced making this demo.

Statik is a static site generator written in Kotlin that is a work in progress. I want to build a templateless static
site generator with code as the user interface. I'll update this page when I publish that.


## How to use
When you load Kastro, by default you will be looking at the current day somewhere in Denver, CO. You can change
the location by dragging the map or manually updating the coordinates. The date can also be changed.

>[!NOTE]
> If you do not see a map you may be on a smaller screen where the settings can only be activated by 
pushing the ![settings](./docs/settings.svg) button.

You can select "Copy Location Link," which will copy a URL that links the demo to a specific location. For example: 

https://yoxjames.github.io/kastro-demo/?latitude=42.88364&longitude=-78.87772

Would link you directly to Buffalo, NY. 

## Reporting bugs
Bug reports for both the demo and Kastro are welcome. However, I am most concerned with bugs in Kastro. If you suspect a
bug you can click the "Copy Link" button in the demo and share it along with the bug report. This will generate a URL with all the params you currently have
set. This includes things like location and the date.

For example:

https://yoxjames.github.io/kastro-demo/?latitude=42.88364&longitude=-78.87772&date=2024-04-27

If clicked would set the demo to the Buffalo, NY area on April 27th, 2024. 

So simply copy the link for any bug you find and log that in your issue 
[kastro-demo](https://github.com/yoxjames/kastro-demo) project.


