chrome.contextMenus.create({
    "title": "Pimp this !",
    "contexts": ["page","selection","link"],
    "onclick" : graburl
  });

  var server;

  function graburl(info)
  {
   var grabbed=info.selectionText;
   if ( grabbed.startsWith("http"))
   {
    notification("Grabbing url by a monkey",grabbed);
    getValueFromUrl("http://" + server + "/admin?link=" + grabbed,true);
   }else
   {
    notification("Error","[" + grabbed + "] not starting with http, bypass");   
   }
}

  $(document).ready(function(){
    
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
         for ( i=0;i<jsonMonkeys.length;i++)
          {
              if ( jsonMonkeys[i].progression == 100 && downloaded.get(jsonMonkeys[i].url) != "100" )
              {
                notification("Finished","[" + jsonMonkeys[i].fileName + "] is downloaded");  
                downloaded.set(jsonMonkeys[i].url,"100"); 
              }
          }
          
        }
         
     }
     

    function log(message)
    {
    console.log(message);
    }


  });


