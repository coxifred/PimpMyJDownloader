 var millimeter;
 var panel;
 var availWidget;
 var currentAvailWidget=0;
 var thetable;
 
$(document).ready(function(){
	loadRegisterWidget();
	
	$("body").delegate(".lockPanel", "click", function(){
		id=$(this).attr("id");
        panel=id.split("_")[1];
		console.log("click on lock panel=" + panel);
		$("#unlockpanel_" + panel).show();
		$("#lockpanel_" + panel).hide();
		$("#refreshPanel_" + panel).show();
		$("#millimeterPanel_" + panel).show();
		
				for ( i=0;i<swiperH.length;i++)
					{
							try{swiperH[i].lockSwipeToNext();swiperH[i].lockSwipeToPrev();}catch (e){ console.log("ERR " + e)};
					}
				try{swiperH.lockSwipeToPrev();swiperH.lockSwipeToNext();}catch (e){};
				
				for ( i=0;i<swiperV.length;i++)
				{
							try{swiperV[i].lockSwipeToNext();swiperV[i].lockSwipeToPrev();}catch (e){console.log("ERR " + e)};
				}
				try{swiperV.lockSwipeToPrev();swiperV.lockSwipeToNext();}catch (e){console.log("ERR " + e)};
			
		
			$("#widgetsContainer_" + panel).find(".lockable").each(function() {
				console.log("Showing container " + $(this));
		        $(this).show();
		    });
	});
	
	$("body").delegate(".refreshPanel", "click", function(){
		console.log('Refresh');
		$("#widgetsContainer_" + panel).children().each(function() {
	        $(this).remove();
	    });
		
		$("#widgetsContainer_" + panel).empty();
		
		loadRegisterWidget();
		$("#widgetsContainer_" + panel).find(".lockable").each(function() {
	        $(this).show();
	    });
	});
	
	$("body").delegate(".millimeterPanel", "click", function(){
		id=$(this).attr("id");
        panel=id.split("_")[1];
		if ( millimeter == 1)
			{
				millimeter=0;
				$("#widgetsContainer_" + panel + "_millimeter").hide();
			}else
				{
				millimeter=1;
				$("#widgetsContainer_" + panel + "_millimeter").show();
				}
		
	});
	
	$("body").delegate(".unlockPanel", "click", function(){
		id=$(this).attr("id");
        panel=id.split("_")[1];
		console.log("click on unlock panel=" + panel);
		$("#unlockpanel_" + panel).hide();
		$("#lockpanel_" + panel).show();
		$("#refreshPanel_" + panel).hide();
		$("#millimeterPanel_" + panel).hide();
		
		 for ( i=0;i<swiperH.length;i++)
			{
					try{swiperH[i].unlockSwipeToPrev();swiperH[i].unlockSwipeToNext();}catch (e){};
			}
			try{swiperH.unlockSwipeToPrev();swiperH.unlockSwipeToNext();}catch (e){};
			for ( i=0;i<swiperV.length;i++)
			{
					try{swiperV[i].unlockSwipeToPrev();swiperV[i].unlockSwipeToNext();}catch (e){};
			}
			try{swiperV.unlockSwipeToPrev();swiperV.unlockSwipeToNext();}catch (e){};
			
		
			$("#widgetsContainer_" + panel).find(".lockable").each(function() {
				console.log("Hiding container " + $(this));
		        $(this).hide();
		    });
			
			// Sauvegarde des coordonnées
			$("#widgetsContainer_" + panel).find(".adrag").each(function() {
				//$(this).draggable( "disable" )
				var left=$(this).css("left");
				var top=$(this).css("top");
				var name=$(this).attr("name");
				//alert("Saving " + name + " at left:" + left + " top:" + top);
				getValueFromUrl("DomoticObjectAction?method=setWidgetPosition&domoticId="+name+"&left=" +left + "&top=" + top,true);
		    });
	});
	
	
	$("body").delegate(".addWidgetPanel", "click", function(){
		
		var availableWidgets=getValueFromUrl("DomoticObjectAction?method=getAvailableWidget",false);
		var html="";
		k=0;
		currentAvailWidget=0;
		if ( availableWidgets != null && availableWidgets != "" )
		 {
		 	availWidget=JSON.parse(availableWidgets);
		 	for ( i=0;i<availWidget.length;i++)
	 		{
		 		
		 		html+="<tr>";
		 		html+="<td><center><img height=16 width=16 src=ImageAction?name=" + availWidget[i].plugin.imagePath + "></td>";
		 		html+="<td>" + availWidget[i].aClass + "</td>";
		 		html+="<td>" + availWidget[i].commentaire  + "</td>";
		 		html+="<td>" + availWidget[i].alias + "</td>";
		 		html+="<td>" + availWidget[i].plugin.commentaire  + "</td>";
		 		html+="<td>" + availWidget[i].plugin.version  + "</td>";
		 		html+="<td><button id=" + i + " class=\"ui active button elementAware\"><i class=\"plus icon\"></i>Add</button></td>";
				html+="</tr>";
				console.log("Add available widget " + availWidget[i].aClass);
	 		}
	
		 	
		 	
		 }
		html+="</div>";
		 $('#ha_elements').html(html);
		 if ( typeof thetable != "undefined" )
			 {
			 thetable.destroy();
			 }
		 thetable=$('#example').DataTable();
		
		
		
		$('#ha_panel_catalog')
		  .modal({onApprove : function(e) {
			if ( e.hasClass("Confirm") )
				{
				}
			}}).modal('show');
		
		
	});
	
	$("body").delegate(".elementAware", "click", function(e){
		var id=this.id;
		var ado=availWidget[id];
		console.log("Adding " + ado);
		var widgetJson=getValueFromUrl("DomoticObjectAction?method=createWidget&domoticId=" + ado.domoticId+"&panel=" + panel,false);
		var widget=JSON.parse(widgetJson);
		displayWidget(widget);
	});
	
	$("body").delegate(".rotateable", "click", function(e){
		var id=this.id;
		console.log("rotate");
		rotateWidget(id,2);
		
	});
	
	$("body").delegate(".rotateableAnti", "click", function(e){
		var id=this.id;
		console.log("rotate");
		rotateWidget(id,-2);
	});
	
	
	$("body").delegate(".adrag", "mouseover", function(){
		id=$(this).attr("id");
		//console.log("mouseover on " + id);
		$("#widgetsContainer_" + panel).find(".adrag").each(function() {
			$(this).css("z-index",100);
	    });
		$(this).draggable();
		$(this).css("z-index",200);
	});
	
	function traceWidgetConsole(widget)
	{
		console.log("widget.domoticId=" + widget.domoticId);
		console.log("widget.zoom=" + widget.zoom);
		console.log("widget.rotate=" + widget.rotate);
	}
	
	function displayWidget(widget)
	{
		traceWidgetConsole(widget);
		$("#drag_" + widget.domoticId).remove();
		var html="<div style=position:absolute;z-index:200;left:" + widget.posX + "px;top:" + widget.posY +"px;width:0%;height:0%; id='drag_" + widget.domoticId + "' class=adrag name='" + widget.domoticId + "' ><div class=lockable style=display:none;height:17px;width:150px;background-color:#1C1C1C;position:relative;top:8px;left:8px ><img class=lockable  height=16px width=16px style=height:16px;display:none;position:absolute;left:1px;z-index:300 src=images/move.png><img class='lockable zoomable-in' id='" + widget.domoticId +"' height=16px width=16px style=height:16px;display:none;position:absolute;left:35px;z-index:300 src=images/zoom-in.png><img class='lockable zoomable-out' id='" + widget.domoticId +"' height=16px width=16px style=height:16px;display:none;position:absolute;left:52px;z-index:300 src=images/zoom-out.png><img class='lockable rotateable' id='"+ widget.domoticId +"'  height=16px width=16px style=height:16px;display:none;position:absolute;left:68px;z-index:300 src=images/rotate.png><img class='lockable rotateableAnti' id='"+ widget.domoticId +"'  height=16px width=16px style=height:16px;display:none;position:absolute;left:85px;z-index:300 src=images/rotate.png><img class='lockable resetable' id='"+ widget.domoticId +"'  height=16px width=16px style=height:16px;display:none;position:absolute;left:102px;z-index:300 src=images/reset.png><img name=" + widget.domoticId + " class=\"lockable removeDomoticWidget\" style=height:16px;display:none;left:-15px;position:absolute;left:135px;z-index:300 height=16px width=16px src=images/trash.png><img id='" +  widget.domoticId + "' class='lockable editable' height='16px' width='16px' style='height:16px;position:absolute;left:17px;z-index:300' src='images/parameters.png'></div><iframe class='not-selectable' style='" + getZoomWidget(widget) + ";" + getRotateWidget(widget) + "' height=" + widget.height + " width=" + widget.width + " scrolling=no frameborder=0 id=\"widget_" + widget.domoticId + "\" srcdoc=\"<!DOCTYPE html>" + widget.contents.replace("@@icon@@",widget.icon) +"\"  /></div>";
		$("#widgetsContainer_" + panel).html($("#widgetsContainer_" + panel).html() + html);
		$("#drag_" + widget.domoticId).css("left",widget.posX + "px");
		$("#drag_" + widget.domoticId).css("top",widget.posY + "px");
		//alert("Displaying widget into widgetsContainer_" + panel + " " + $("#widgetsContainer_" + panel).html());
		refreshWidgetZoomAngle(widget);
		
		$("#drag_" + widget.domoticId).draggable({ iframeFix: true });
		//console.log("drag on " + widget.domoticId);
		
	}
	
	function getZoomWidget(widget)
	{
		console.log("fct_getZoomWidget:" + widget.zoom);
		currentZoom=widget.zoom;
		console.log("return fct_getZoomWidget:" + "zoom: "+ currentZoom +";-moz-transform:scale("+ currentZoom +");-moz-transform-origin: 0 0;-o-transform:scale("+ currentZoom +");-o-transform-origin: 0 0;-webkit-transform: scale("+ currentZoom +");-webkit-transform-origin: 0 0");
		return "zoom: "+ currentZoom +";-moz-transform:scale("+ currentZoom +");-moz-transform-origin: 0 0;-o-transform:scale("+ currentZoom +");-o-transform-origin: 0 0;-webkit-transform: scale("+ currentZoom +");-webkit-transform-origin: 0 0";
	}
	function getRotateWidget(widget)
	{
		currentRotate=widget.rotate;
		return "-ms-transform:rotate(" + currentRotate +"deg);-webkit-transform:rotate(" + currentRotate +"deg);transform:rotate(" + currentRotate +"deg);";
	}
	function rotateWidget(id,angle)
	{
		var currentRotate=parseInt(getRotationDegrees($("#widget_" + id)));
		var currentZoom=parseFloat($("#widget_" + id).css("zoom"));
		currentRotate+=angle;
		//alert("#widget_" + id + " currentRotate=" + currentRotate);
		var currentZoom=parseFloat($("#widget_" + id).css("zoom"));
		rotate($("#widget_" + id),currentRotate,currentZoom);
		getValueFromUrl("DomoticObjectAction?method=setWidgetZoomAndRotate&domoticId="+id+"&zoom=" +currentZoom + "&rotate=" + currentRotate,true);
	}
	
	$("body").delegate(".zoomable-in", "click", function(){
		var id=this.id;
		zoom(id,0.1);
	});
	

	$("body").delegate(".zoomable-out", "click", function(){
		var id=this.id;
		zoom(id,-0.1);
	});
	
	
	function zoom(id,coeff)
	{
		var widgetJson=getValueFromUrl("DomoticObjectAction?method=getWidget&domoticId=" +id+"&panel=" + panel,false);
		var widget=JSON.parse(widgetJson);
		console.log("id:" + id);
		console.log($("#widget_" + id));
		currentZoom=widget.zoom;
		console.log("Zoom of #widget_" + id + ":"  + currentZoom);
		if ( typeof currentZoom== "undefined")
			{
			console.log("Fixing currentZoom to 0 because zoom of #widget_" + id + " is "  + currentZoom);
			currentZoom=0;
			console.log("Fixing to 0 #widget_" + id + ":"  + currentZoom);
			}
		

		currentZoom=parseFloat(currentZoom);
		var currentRotate=parseInt(getRotationDegrees($("#widget_" + id)));
		console.log("currentZoom:" + currentZoom);
		console.log("currentRotate:" + currentRotate);
		console.log("coeff:" + coeff);
//		$("#widget_" + id).css("height",(currentHeight + 20));
//		$("#widget_" + id).css("width",(currentWidth + 20));
		
		
		var ratioH=widget.height * 1.1;
		var ratioW=widget.width * 1.1;
		console.log("widget.height:" + widget.height);
		console.log("widget.width:" + widget.width);
		currentZoom+=coeff;
		
		currentHeight=parseInt($("#widget_" + id).css("height"));
		$("#widget_" + id).css("height",parseInt(ratioH));
		currentWidth=parseInt($("#widget_" + id).css("width"));
		$("#widget_" + id).css("width",parseInt(ratioW ));
		
		rotate($("#widget_" + id),currentRotate,currentZoom);
		getValueFromUrl("DomoticObjectAction?method=setWidgetZoomAndRotate&domoticId="+id+"&zoom=" +currentZoom + "&rotate=" + currentRotate,true);
		getValueFromUrl("DomoticObjectAction?method=setWidgetSize&domoticId="+id+"&width=" +parseInt(ratioW) + "&height=" + parseInt(ratioH),true);
	}
	
	
	$("body").delegate(".removeDomoticWidget", "click", function(){
		var id=$(this).attr("name");
		$("#drag_" + id).remove();
		getValueFromUrl("DomoticObjectAction?method=deleteWidget&domoticId=" + id,true);
		
	});	
	
	$("body").delegate(".resetable", "click", function(){
		var domoticId=this.id;
		// Reset 
		getValueFromUrl("DomoticObjectAction?method=resetWidget&domoticId=" +domoticId+"&panel=" + panel,false);
		// Reload
		var widgetJson=getValueFromUrl("DomoticObjectAction?method=getWidget&domoticId=" +domoticId+"&panel=" + panel,false);
		var widget=JSON.parse(widgetJson);
		currentWidget=widget;
		$("#drag_" + currentWidget.domoticId).remove();
		displayWidget(currentWidget);
		$("#widgetsContainer_" + panel).find(".lockable").each(function() {
	        $(this).show();
	    });
		
	});
	
	
	function refreshWidgetZoomAngle(widget)
	{
		var currentZoom=parseFloat(widget.zoom);
		var currentRotate=parseInt(widget.rotate);
		rotate($("#widget_" + widget.domoticId),currentRotate,currentZoom);
	}

	
	 
	
	
	function loadRegisterWidget()
	{
		
		var panelList=getValueFromUrl("DomoticObjectAction?method=getAllPanelId",false);
		var jsonList=JSON.parse(panelList);
		for ( i=0;i<jsonList.length;i++)
 		{
			
			localpanel=jsonList[i];
			$("#modalWidget_" +localpanel).hide();
			$("#modalWidgetEdit_" +localpanel).hide();
			
	 		var registerWidgets=getValueFromUrl("DomoticObjectAction?method=getAllWidgets&panel="+localpanel,false);
			
				var html="";
				k=0;
				if ( registerWidgets != null && registerWidgets != "" )
				 {
				 	var json=JSON.parse(registerWidgets);
				 	for ( l=0;l<json.length;l++)
			 		{
				 		panel=localpanel;
				 		displayWidget(json[l]);
				 	}
				 }
 		}
	
	}
	
	
	
});