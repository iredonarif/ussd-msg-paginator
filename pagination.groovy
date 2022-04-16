/**
* Gets the number of menus
*
* @param {string} message: the message to display with pagination
* @param {integer} nc: number of caracters
* @param {string} prevMsg: the previous message text
* @param {string} nextMsg: the next message text
* @param {string} backMainMsg: back to main menu text. Not required
*
* The +1 on the length of a navigation text is for newline (\n caracter)
*
* @return {integer} number of menus that will be displayed with this message
*/
def getNumberOfMenus(message, nc, prevMsg, nextMsg, backMainMsg=null) {
    initalMsgLen = message.length()
    nextMsgLen = nextMsg.length() + 1
    prevMsgLen = prevMsg.length() + 1

    /** Number of menus with the inital message **/
    n = (initalMsgLen % nc == 0) ? (initalMsgLen / nc) : intDiv(initalMsgLen, nc) + 1

    /** Number of menus with initial message + navigation options **/
    def nbMenus = 1
    def int allMsgLen = initalMsgLen
    if (n > 2) {
        iMenu = n - 2
        iNavsLen = iMenu * nextMsgLen + prevMsgLen
        allNavsLen = nextMsgLen + 2 + prevMsgLen + iNavsLen

        allMsgLen = initalMsgLen + allNavsLen
    }
    else if (n == 2) {
        allMsgLen = initalMsgLen + nextMsgLen + prevMsgLen
    }
    else {
        allMsgLen = initalMsgLen + (backMainMsg != null ? (backMainMsg.length() + 1) : prevMsgLen)
    }
    nbMenus = (allMsgLen % nc) == 0 ? (allMsgLen / nc) : intDiv(allMsgLen, nc) + 1

    return nbMenus
}

/**
* Gets the navigation text that will be displayed at the bottom of the menu
*
* @param {integer} numMenu: the current menu number
* @param {integer} nbMenus: number of menus
* @param {string} prevMsg: previous message
* @param {string} nextMsg: next message
* @param {string} backMainMsg: back to main message
*
* @return {string} navigation text
*/
def getNavigationText(numMenu, nbMenus, prevMsg, nextMsg, backMainMsg=null) {
    message = ""
    if (numMenu == 1 && nbMenus == 1) {
        message = (backMainMsg != null) ? "\n${backMainMsg}" : "\n${prevMsg}"
    }
    if (numMenu == 1 && nbMenus > 1) {
        message = (backMainMsg != null) ? "\n${nextMsg}\n${backMainMsg}" : "\n${nextMsg}\n${prevMsg}"
    }
    else if (numMenu == nbMenus && nbMenus > 1) {
        message = "\n${prevMsg}"
    }
    else if (numMenu > 1 && nbMenus > 2) {
        message = "\n${nextMsg}\n${prevMsg}"
    }
    return message
}

/**
* Gets the end position. Will be used to substract a sub message from the message
*
* @param {integer} numMenu: num menu
* @param {integer} nbMenus: number of menus
* @param {integer} nc: number of caracters
* @param {string} prevMsg: previous message
* @param {string} nextMsg: next message
* @param {string} backMainMsg: back to main message. Not required
* @param {integer} position: start position
*
* @return {integer} end position value
*/
def getEndPosition(numMenu, nbMenus, nc, prevMsg, nextMsg, backMainMsg=null, position) {
    return position + (nc - getNavigationText(numMenu, nbMenus, prevMsg, nextMsg, backMainMsg).length())
}

/**
* Gets the message to display on a specific menu
*
* @param {integer} numMenu
* @param {string} message: global message
* @param {integer} nc: number of caracters
* @param {string} prevMsg: previous message
* @param {string} nextMsg: next message
* @param {string} backMainMsg: back to main message. Not required
* @param {integer} position: start position
*
* @param {boolean} updatePosition: Used as an indicator to update the JUMP value in the session.
* JUMP is the difference between startPosition & endPosition. Update position must be false if
* the user types a wrong caracter for navigation
*
* @param {boolean} isBack: Used as an indicator to update CURSOR_POSITION in the session.
* The isBack value must be true if the user chooses to back on the previous menu
*
* @return {string} message to display on a specific menu
*/
def getMsgToDisplay(numMenu, message, nc, prevMsg, nextMsg, backMainMsg=null, position, updatePosition=true, isBack=false) {

    nbMenus = getNumberOfMenus(message, nc, prevMsg, nextMsg, backMainMsg)
    session.data.NB_MENUS = nbMenus
    navigationText = getNavigationText(numMenu, nbMenus, prevMsg, nextMsg, backMainMsg)

    messageToDisplay = ""

    // We are on the last menu
    if (numMenu == nbMenus || numMenu > nbMenus) {
        messageToDisplay = "${message.substring(position) + navigationText}"
    }
    else {
        newPosition = getEndPosition(numMenu, nbMenus, nc, prevMsg, nextMsg, backMainMsg, position)
        if (updatePosition) {
            session.data.JUMP = newPosition - position
        }
        if (!isBack) {
            session.data.CURSOR_POSITION = newPosition
        }
        else {
            p = newPosition - session.data.JUMP
            session.data.CURSOR_POSITION = p >= 0 ? session.data.JUMP : p
        }
        messageToDisplay = "${message.substring(position, newPosition)}${navigationText}"
    }
    return messageToDisplay
}

/**
* Integer division
*
* Returns the integer part of a division value
*/
def intDiv(n1, n2) {
    return Integer.parseInt((n1/n2).toString().split("\\.")[0])
}
