/**
 * Script contenant des fonctions js pratiques
 */

/**
 * Permet de récupérer un parametre passé dans l'url
 * @param str
 * @returns
 */
function getVal(str) {
    var v = window.location.search.match(new RegExp('(?:[\?\&]'+str+'=)([^&]+)'));
    return v ? v[1] : null;
}




function confirm(icon,message,callbackConfirm,callbackCancel)
{
	$('#confirm_icon').html("<i class=\"" + icon + " icon\"></i>");
	$('#confirm_message').html(message);
	var retour;
	$('#confirm')
	  .modal({onApprove : function(e) {
		if ( e.hasClass("Confirm"))
			{
			console.log("Confirmed");
			if ( callbackConfirm) callbackConfirm();
			}else
				{
				if ( callbackCancel ) callbackCancel();
				}
		}}).modal('show');
	
}

function url(s) {
    var l = window.location;
    return ((l.protocol === "https:") ? "wss://" : "ws://") + l.hostname + (((l.port != 80) && (l.port != 443)) ? ":" + l.port : "") + "/" + s;
}

function message(message)
{

	$("#messageContent").html(message);
	$('#message').show();	
	$('#message').removeClass('transition hidden');
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


function setUrl(url)
{
	document.location=url;
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
//					alert("ERROR ! " + errorThrown+'\n'+status+'\n'+xhr.statusText);
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
	return retour;
	}
}

/**
 * Function Ajax
 * @param theUrl
 * @param asyncMode
 * @returns
 */
function getValueFromUrlHidden(theUrl,asyncMode,data) {
	console.log("getValueFromUrlHidden theUrl=" + theUrl + " asyncmode=" + asyncMode + " data=" + data );
	var retour; 
	var data = {
			customContents: data
			};
	try 
	{
	var request = $
			.ajax({
				url : theUrl,
				type : "POST",
				data : data,
				async : asyncMode,
				
					error:function(xhr, status, errorThrown) {
					//alert("ERROR ! " + errorThrown+'\n'+status+'\n'+xhr.statusText);
			        } 
			});
			
	request
			.done(function(msg) {
				retour = trim(msg);
				
			});
			return retour;
			
}	catch (err)
{
	retour = "ERROR";
	return retour;
	}
}

/**
 * Detect si c'est un mobile ou non
 * @returns
 */
function detectmob() { 
	 
	 if( navigator.userAgent.match(/Android/i)
	 || navigator.userAgent.match(/webOS/i)
	 || navigator.userAgent.match(/iPhone/i)
	 || navigator.userAgent.match(/iPad/i)
	 || navigator.userAgent.match(/iPod/i)
	 || navigator.userAgent.match(/BlackBerry/i)
	 || navigator.userAgent.match(/Windows Phone/i)
	 ){
	    return true;
	  }
	 else {
	    return false;
	  }
	}

	if ( detectmob() )
	{
		//includeCss('css/mobile/HomeAutomate.css');
	}
	else
	{
	//	includeCss('css/pc/HomeAutomate.css');	
	}

	
	function getRotationDegrees(obj) {
		  var matrix = obj.css("-webkit-transform") ||
		  obj.css("-moz-transform")    ||
		  obj.css("-ms-transform")     ||
		  obj.css("-o-transform")      ||
		  obj.css("transform");
		  if(matrix !== 'none' && (typeof(matrix) != "undefined")) {
		    var values = matrix.split('(')[1].split(')')[0].split(',');
		    var a = values[0];
		    var b = values[1];
		    var angle = Math.round(Math.atan2(b, a) * (180/Math.PI));
		  } else { var angle = 0; }
		  return (angle < 0) ? angle +=360 : angle;
		}
	function rotate($el, degrees,scale) {
	    $el.css({
	  '-webkit-transform' : 'rotate('+degrees+'deg) scale(' + scale +')',
	     '-moz-transform' : 'rotate('+degrees+'deg) scale(' + scale +')',  
	      '-ms-transform' : 'rotate('+degrees+'deg) scale(' + scale +')',  
	       '-o-transform' : 'rotate('+degrees+'deg) scale(' + scale +')',  
	          'transform' : 'rotate('+degrees+'deg) scale(' + scale +')',  
	               'zoom' : scale

	    });
	}
	