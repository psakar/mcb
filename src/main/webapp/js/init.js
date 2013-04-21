function init_top_frame (metatopic, language) {

	if(top.menu && top.menu.go) {
  		top.menu.setLoc(document.location.pathname);
 	}
 	
	window.defaultStatus=document.location.pathname; 
	if(top.showNavBar) top.showNavBar ('menu');


	try {  	
  		metatopics=parent.frames.meta.metatopics.childNodes.length;
		
		for(var i=0;i<metatopics;i++) { 
			if(parent.frames.meta.metatopics.childNodes.item(i).id==metatopic) {
 				if (parent.frames.meta.metatopics.childNodes.item(i).className=="MetaButton")
 					{parent.frames.meta.metatopics.childNodes.item(i).className="MetaButtonhl";}
 				}
 			else {
 				if (parent.frames.meta.metatopics.childNodes.item(i).className=="MetaButtonhl")
 					{parent.frames.meta.metatopics.childNodes.item(i).className="MetaButton";}
 				}
 		}
 		
   	    metatopics=parent.frames.meta.languages.childNodes.length;
   	    
 		for(var i=0;i<metatopics;i++) { 	
		if(parent.frames.meta.languages.childNodes.item(i).id==language)
 				{if (parent.frames.meta.languages.childNodes.item(i).className=="MetaButton")
 					{parent.frames.meta.languages.childNodes.item(i).className="MetaButtonhl";}
 				}
 			else 
 				{if (parent.frames.meta.languages.childNodes.item(i).className=="MetaButtonhl")
 					{parent.frames.meta.languages.childNodes.item(i).className="MetaButton";}
 				}
 		}
	}		
 		
	catch(e)
	{return -1;}		
}
