chrome.contextMenus.create({
    "title": "Pimp this !",
    "contexts": ["page","selection","link"],
    "onclick" : graburl
  });

  var server;

  function graburl(info)
  {
   var grabbed=info.selectionText;
   var url=info.linkUrl;
   var selected=url;
   if ( typeof grabbed != "undefined" && grabbed.startsWith("http") )
   {
	selected=grabbed;
   }	   
   
   if ( selected.startsWith("http"))
   {
    notification("Grabbing url by a monkey",selected);
    getValueFromUrl("http://" + server + "/admin?link=" + selected,true);
   }else
   {
    notification("Error","[" + selected + "] not starting with http, bypass");   
   }
  }
 
  $(document).ready(function(){
	reload();  
	 
    setInterval(function(){reload();},5000); 
    
    
    function reload()
     {
        server=localStorage.getItem('pimpMyJDownloaderServer');
        if ( server != undefined)
        {
        var feedback=getValueFromUrl("http://" + server + "/admin?state=yes",true,parseDownload);
        }else
        {
            log("server is undefined");
        }
     }

     function parseDownload(jsonMessage)
     {
         if ( jsonMessage == undefined)
         {
            $("#content").html("Error in query server " + server);
           
         }else{
         
         jsonMonkeys=JSON.parse(jsonMessage);
         var html="";
		 var globalPercent=0;
		 var count=0;
         for ( i=0;i<jsonMonkeys.length;i++)
          {
              if ( jsonMonkeys[i].progression == 100 && downloaded.get(jsonMonkeys[i].url) != "100" )
              {
                notification("Finished","[" + jsonMonkeys[i].fileName + "] is downloaded");  
                downloaded.set(jsonMonkeys[i].url,"100"); 
              }
			  if (  jsonMonkeys[i].progression > 0 )
			  {
				  globalPercent+=jsonMonkeys[i].progression;
				  count++;
			  }
          }
          //Compute %
		  globalPercent=parseInt(Math.floor((globalPercent/count % 10)) * 10);
		  
		  chrome.runtime.sendMessage({
					action: 'updateIcon',
					value: globalPercent
		  });
		  //notification("Setting /img/" + globalPercent +".png" );
          chrome.browserAction.setIcon({path: "/img/" +globalPercent+".png"});
		  
        }
         
     }
     

    function log(message)
    {
    console.log(message);
    }


  });
