# About
As the number of characters on USSD menu is limited, i developed a module to paginate the message that will be displayed.
This module is developed in Groovy; but this logic can be used to implement the pagination in another language.

## Usage
In the module, `getMsgToDisplay` is the main function. So to paginate a text, you have to call it with the mandatory parameters. Please take a look in the code to see how to use it. At the end of the execution of this function, the following variables will be saved in the user session:

* NB_MENUS: Number of menus after splitting text
* JUMP: The difference between the start position and the end position (the positions used when subtracting text)
* CURSOR_POSITION: Is the start position for a next pagination

_In my case, session variables are saved in session.data; and session.data.CURSOR_POSITION will return the value of CURSOR_POSITION._

### Example of this function call

------
def msg = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
cillum dolore eu fugiat nulla pariatur"

getMsgToDisplay(2, msg, 180, "0 Back", "* Next", "00 Main menu", 174)
------

When the user type a navigation character (to back or to go to next) or wrong navigation character, make sure to pass the rigth parameters to the pagination function otherwise you will get a wrong result.

Note that for the first call, the postion value must be 0.

## New Version
I developed a new version of this module where the navigation characters are parameters; but this version is not published.

## Note

Please if you need any clarification regarding this repo or if you have a best solution, you can contact me on my email. Thanks 😺 !
