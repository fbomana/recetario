/**
 * We keep the dimensions of the viewport updated in the vars recetario_x and recetario_y
 */

var recetario_w = window, recetario_e = document.documentElement, recetario_g = document.getElementsByTagName('body')[0], 
    recetario_x = recetario_w.innerWidth || recetario_e.clientWidth || recetario_g.clientWidth,
    recetario_y = recetario_w.innerHeight|| recetario_e.clientHeight|| recetario_g.clientHeight;
    
document.onresize = function( event )
{
    var recetario_x = recetario_w.innerWidth || recetario_e.clientWidth || recetario_g.clientWidth,
    recetario_y = recetario_w.innerHeight|| recetario_e.clientHeight|| recetario_g.clientHeight;
}

/**
 * Changes the class of the selected menu option
 * @returns {undefined}
 */
function lightMenu()
{
    var path = window.location.pathname;
    var id = "";
    if ( path == "/recipes/SearchRecipes")
    {
        id = "search";
    }
    else if ( path == "/recipes/NewRecipe")
    {
        id = "new";
    }
    
    if ( id )
    {
        document.getElementById( id ).className="menuSelected";
    }
}
/**
 * Create an empty copy of a DOM element and replace the original by it. Returns the
 * new object.
 * 
 * @param {type} id
 * @returns {unresolved}
 */
function ClearOptionsFast( id )
{
    var selectObj = document.getElementById(id);
    var selectParentNode = selectObj.parentNode;
    var newSelectObj = selectObj.cloneNode(false);
    selectParentNode.replaceChild(newSelectObj, selectObj);
    return newSelectObj;
}

/**
 * Function that make a post call to a service using paramArray and url.
 * When the call is completed it invokes the callback fuction whith two parameters
 * a boolean value indicating if it end up ok and the HttpServletRequest
 * @param {type} url
 * @param {type} paramArray
 * @param {type} callback
 * @returns {undefined}
 */
function post( url, paramArray, callback )
{
    var xhr = new XMLHttpRequest();
    var params = "";

    for ( var i = 0; paramArray && i < paramArray.length; i = i + 2 )
    {
        if ( i > 0 )
        {
            params+="&";
        }
        params += paramArray[i] + "=" + encodeURIComponent( paramArray[i+1] );
    }
    xhr.open('post', url);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.setRequestHeader("Content-length", params.length);
    xhr.setRequestHeader("Connection", "close");

    // Track the state changes of the request.
    xhr.onreadystatechange = function () 
    {
        var DONE = 4;
        var OK = 200;
        if (xhr.readyState === DONE) 
        {
            callback( xhr.status === OK, xhr );
        }
    };
    xhr.send( params );
}
