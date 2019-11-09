
var live=1;

$(document).ready(function(){

            log("Loading extension");
            reload();
            var server=localStorage.getItem('pimpMyJDownloaderServer')
            if ( server == undefined )
            {
            notification("Welcome Home Cornelius", "Please enter your server adress");
            }else{
            log('Found server ' + server);
            $("#serverInfo").val(server);
            }
                
            
            $("body").delegate("#save", "click", function(){
                log('Writing in local storage ' + $("#serverInfo").val());
                localStorage.setItem('pimpMyJDownloaderServer',$("#serverInfo").val());
                // Testing server
                getValueFromUrl("http://" + $("#serverInfo").val() + "/admin?version=?",true,checkVersion);
		    
             });	

             $("body").delegate("#pimp", "click", function(){
               
                // Sending url
                getValueFromUrl("http://" + server + "/admin?link=" +  $("#url").val(),true,cleanInput);
             });
             
             $("body").delegate("#linkServer", "click", function(){
                var win=windows.open("http://" + server,'_blank');
                win.focus();
             });

             $("body").delegate(".deletable", "click", function(){
		    	var id=this.id;
		    	getValueFromUrl("http://" + server + "/admin?clear=" + id,function(){
                downloaded.delete(id);
                reload();
                });
		    }); 
	 
            $("body").delegate(".info", "click", function(){
                    var id=this.id;
                    var info=getValueFromUrl("http://" + server + "/admin?info=" + id,true,function(mesg){
                        alert(JSON.stringify(JSON.parse(mesg), null, 4));
                    });
            });
	        
	 
	 
            function cleanInput()
            {
                $("#url").val("");
            }


            setInterval(
                function(){ 
                             if ( live == "1" )
                                 {
                                 reload();
                                
                                 }
                            }
                    ,2000); 

            function reload()
             {
             
              var feedback=getValueFromUrl("http://" + server + "/admin?state=yes",true,parseDownload);
             
             }

             function checkVersion(version)
             {
                if ( version != undefined && version != "ERROR")
                {
                notification("Good news !", "Server has been setted and responding. Version is " + version);
                $("#health").html("<div class=\"ui red label\"><i class=\"thumbs up icon\"></i>OK</div>");
                }else
                {
                notification("Problem..", "Server " + server + " seems to be down, are you sure ?");
                 $("#health").html("<div class=\"ui green label\"><i class=\"thumbs down icon\"></i>OK</div>");
                }
               
             }

             function parseDownload(jsonMessage)
             {
                 if ( jsonMessage == undefined)
                 {
                    $("#content").html("Error in query server " + server);
                    $("#health").html("<div class=\"ui red label\"><i class=\"thumbs down icon\"></i>OK</div>");
                 }else{

                    $("#health").html("<div class=\"ui green label\"><i class=\"thumbs up icon\"></i>OK</div>");
                 jsonMonkeys=JSON.parse(jsonMessage);
                 var html="<table>";
                 for ( i=0;i<jsonMonkeys.length;i++)
                  {
                      
                      html+="<tr>";
                          html+="<td><button id=\"" + jsonMonkeys[i].url + "\" class=\"mini ui primary button deletable\">Clean</button><button id=\"" + jsonMonkeys[i].url + "\" class=\"mini ui primary button info\">Infos</button>";
                          html+=jsonMonkeys[i].fileName + " ";
                          html+=jsonMonkeys[i].speed + " Kb/s ";
                          html+="<div style=\"display:inline;height:15px;width:30px\" class=\"ui indicating progress\" data-percent=\"" + jsonMonkeys[i].progression + "\"><div class=\"bar\" style=\"height:15px;transition-duration: 300ms; width: " +jsonMonkeys[i].progression +"%\"><div class=\"progress\">"+ jsonMonkeys[i].progression+ "%</div></div></div></td>";
                
                       html+="</tr>";	 		
                  }
                  html+="</table>"
                  $("#content").html(html);
                }
                 
             }
             

            function log(message)
            {
            console.log(message);
            }

});
