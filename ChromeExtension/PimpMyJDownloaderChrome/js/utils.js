/**
 * Script contenant des fonctions js pratiques
 */

var downloaded = new Map();

/**
 * Permet de récupérer un parametre passé dans l'url
 * @param str
 * @returns
 */
function getVal(str) {
    var v = window.location.search.match(new RegExp('(?:[\?\&]'+str+'=)([^&]+)'));
    return v ? v[1] : null;
}



function notification(messageTitle,messageContent)
{
	var notification = new Notification(messageTitle, {
		icon: 'img/monkey.png',
		   body: messageContent,
	   });
}


function url(s) {
    var l = window.location;
    return ((l.protocol === "https:") ? "wss://" : "ws://") + l.hostname + (((l.port != 80) && (l.port != 443)) ? ":" + l.port : "") + "/" + s;
}


/**
 * Fonction permettant de lancer des fonctions en async
 * @param your_function
 * @param callback
 * @returns
 */
function async(your_function, callback) {
	$("#working").show();
    setTimeout(function() {
        your_function();
        if (callback) {callback();};$("#working").hide();
    }, 0);
}



function trim (myString)
{
return myString.replace(/^\s+/g,'').replace(/\s+$/g,'');
} 



/**
 * Function Ajax
 * @param theUrl
 * @param asyncMode
 * @returns
 */
function getValueFromUrl(theUrl,asyncMode,myfunction) {
	var retour; 
	try 
	{
	var request = $
			.ajax({
				url : theUrl,
				type : "POST",
				data : {},
				async : asyncMode,
				dataType : "html",
					error:function(xhr, status, errorThrown) {
						//console.log(xhr,status,errorThrown);
						retour="ERROR";
			        } 
			});
			
	request
			.done(function(msg) {
				if ( typeof myfunction != "undefined" )
					{
					myfunction(msg);
					}else
						{
				retour = trim(msg);
						}
				
			});
			return retour;
			
}	catch (err)
{
	retour = "ERROR";
	//console.log(err);
	return retour;
	}
}



	