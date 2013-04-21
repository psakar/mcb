debug = 0;
ie4=document.all;
ns4=document.layers;

if (debug>0) alert('menu.js ie4' + ie4 + ' ns4 ' + ns4);

window.go = false; //true;
menuIsLoaded = false;
contentFrame = top.content;

openImg="open.gif";
closeImg="close.gif";

//************ COLORS *************************
//modify the following values for level1 navigation colors
//bgcol defines the back color, hlcol defines the roll-over color
bgCol="#ffcc33";
hlCol="#fff5cc";

//modify the following values for level2 navigation colors
//bgcol defines the back color, hlcol defines the roll-over color
bgCol2="#FFEFAD";
hlCol2="#fff5cc";

//modify the following values for level2 navigation colors
//bgcol defines the back color, hlcol defines the roll-over color
bgCol3="#FFF7D6";
hlCol3="#fff5cc";


//Colors for selected
clocCol="#fffbf0";


offsetTop = 150; // default top margin
menuWidth = 166;
bottomMargin = 20;







if(go) {
  toglS='<A HREF="javascript:void(0)" ONCLICK="togl(this)" TARGET="_self">';
  toglE='</A>';
  document.write("<STYLE TYPE='text/css'>.l1,.l2,.l3,.l4,.lx{position:absolute;visibility:hidden;width:"+menuWidth+"}</STYLE>");
}

// menuItem object... Modified Bob Heida 04/2002 to comply with the Company Direct Guidelines
function mItem(name){
  this.name=name;
  this.level=(name.length-1);
  this.open=this.visible=this.curr=false;

  this.style=getElementById(name).style;
  if(ie4) {
	if (this.level==1) {this.style.backgroundColor=bgCol;};
   	if (this.level==2) {this.style.backgroundColor=bgCol2;};
   	if (this.level==3) {this.style.backgroundColor=bgCol3;};
    this.height=self.document.all[name].offsetHeight;
  } else if(ns4){
	if (this.level==1) {this.style.bgColor=bgCol;};
    if (this.level==2) {this.style.bgColor=bgCol2;};
    if (this.level==3) {this.style.bgColor=bgCol3;};
    this.height=document.layers[name].clip.height;
  }


}



function mItemSetBG(color){
  if(ie4) this.style.backgroundColor=color;
  else if(ns4) this.style.bgColor=color;
}


function mItemHide(){this.style.visibility="hidden";this.visible=false;}
function mItemShow(){this.style.visibility="visible";this.visible=true;}
function mItemMove(dist){this.style.top=this.t=parseInt(this.style.top)+dist;}
function mItemMoveTo(t){this.style.top=this.t=t;}
mItem.prototype.setBG = mItemSetBG;
mItem.prototype.hide = mItemHide;
mItem.prototype.show = mItemShow;
mItem.prototype.move = mItemMove;
mItem.prototype.moveTo = mItemMoveTo;
// --- end menuItem object

// findlocation
function setLoc(loc){
  if (debug > 0) alert('set loc to ' + loc);
  window.onerror=function (){return true;};
  if (!loc) {
  	loc=document.location.pathname;
  }
  window.onerror=function () {return false;};
  var locIdx=-1;
  if (!menuIsLoaded) {
    menu_init();
  }
	//alert('menu ' + document.getElementById('menu');
	//alert('menu ' + menu);
  for(var i=0; i < menu.length;i++){
		if (debug > 1) alert('menu[i] ' + menu[i]);
    if(loc.indexOf(menu[i].href)!=-1){
      locIdx=i;
      menu[i].setBG(clocCol);
      menu[i].curr=true;
      if(!menu[i].visible){
        var idx=1;var j=0;
        while(idx++<menu[i].name.length){
          parentMenu=menu[i].name.substring(0,idx);
          while(menu[j].name!=parentMenu){j++;}
          if(!menu[j].open)togl(null,j);
        }
      }
    }
    else
    {
    	if(menu[i].curr)
    	{	if (menu[i].level==1) {menu[i].setBG(bgCol);};
    		if (menu[i].level==2) {menu[i].setBG(bgCol2);};
    		if (menu[i].level==3) {menu[i].setBG(bgCol3);};

    		menu[i].curr=false;
    	}
    }
  }
  var scrollOffset=(ie4)? document.body.scrollTop :  window.pageYOffset;
  var frameHeight=(ie4)? document.body.clientHeight : window.innerHeight;
  var locTop=(locIdx!=-1) ? menu[locIdx].t : 0;
  if(locTop > (scrollOffset + frameHeight)) scrollTo(0,locTop+menu[locIdx].height-frameHeight);
  else if(locTop<scrollOffset) scrollTo(0,locTop);
  return(1);
	if (debug > 1) alert('loc end ' + loc);
}




// rollover highlights
function hl(lnk){
//menu[lnk.idx].setBG(hlCol)
	if (menu[lnk.idx].level==1) {menu[lnk.idx].setBG(hlCol);};
	if (menu[lnk.idx].level==2) {menu[lnk.idx].setBG(hlCol2);};
	if (menu[lnk.idx].level==3) {menu[lnk.idx].setBG(hlCol3);};
}


function ll(lnk)
{
	if (menu[lnk.idx].curr)
	{ menu[lnk.idx].setBG(clocCol);
	}
	else
	{
	if (menu[lnk.idx].level==1) {menu[lnk.idx].setBG(bgCol);};
	if (menu[lnk.idx].level==2) {menu[lnk.idx].setBG(bgCol2);};
	if (menu[lnk.idx].level==3) {menu[lnk.idx].setBG(bgCol3);};
	}
}


function hlns(e){hl(e.target);};
function llns(e){ll(e.target);};


// toggle function
function togl(lnk,idx){
  if(!menu)return(0);
  var offset=0;
  var n=(lnk)?lnk.idx:idx;
  var clicked=menu[n];
  var branch=clicked.name;
  var frameHeight=(ie4)? document.body.clientHeight : window.innerHeight;

  menuBottom=clicked.t+clicked.height;
  if(clicked.open){ //close
    for(var i=n+1;i<menu.length;i++){
      var mi=menu[i];
      if(mi.name.indexOf(branch)!= -1 && mi.visible){
        mi.open=false;
        if(mi.arrow)mi.arrow.src=openImgPath(mi.arrow.src);//oImg.src
        mi.hide();
        mi.moveTo(0);
        offset+=mi.height;
      }
      else if(menu[i].visible){
	    menu[i].move(-offset);
        menuBottom+=menu[i].height;
      }
    }
    menuBottom+=bottomMargin;
    if(menuBottom<frameHeight)window.scrollTo(0,0);
    else if(ns4 && menuBottom < window.pageYOffset + frameHeight)window.scrollTo(0,menuBottom-frameHeight-3);
    if(clicked.arrow) clicked.arrow.src=openImgPath(clicked.arrow.src);
    clicked.open=false;
    if(ns4)redraw();
  }
  else{//open
    for(i=n+1;i<menu.length;i++){
      var mi=menu[i];
      if((mi.name.indexOf(branch)!= -1)&& (mi.level==clicked.level+1)){
        mi.moveTo(menuBottom);
        mi.show();
      }
      else if(mi.visible)mi.moveTo(menuBottom);
      if(mi.visible)menuBottom+=mi.height;
    }
    if(clicked.arrow)clicked.arrow.src=closeImgPath(clicked.arrow.src);
    clicked.open=true;
    menuBottom += bottomMargin;
  }
  window.document.height=menuBottom;

  return(0);
}

function openMenu(){
  var idx=-1;
  for(var i=0;i<defaultOpen.length;i++){
    while(menu[++idx]) {if(defaultOpen[i]==menu[idx].name){togl(null,idx);break;}}
  }
}

function openImgPath(url){
  if(url.indexOf("close.gif")==-1)return(url);
  else return (url.substring(0,url.indexOf("close.gif"))+"open.gif");
}
function closeImgPath(url){
  if(url.indexOf("open.gif")==-1)return(url);
  else return (url.substring(0,url.indexOf("open.gif"))+"close.gif");
}

function redraw(){
  for(var i=0;i<menu.length;i++) if(menu[i].visible){menu[i].hide();menu[i].restore=true;};
  for(var i=0;i<menu.length;i++) if(menu[i].restore){menu[i].show();menu[i].restore=false;};
}

// init
//defaultOpen=null
var menu;

function menu_init(){
	menu = new Array();
  if (debug > 1) alert('menu init ie4 ' + ie4);
  var tIdx = 0;
  var menu_table = null;
  try {
	  if (ie4) {
		  menu_table = document.getElementById('menu');
		} else if(ns4) {
	 	  menu_table = document.getElementById('menu');
	  }
 	} catch (e) {
 	  if (debug>0) alert('Can not find menu ' + e.message);
 	  return;
 	}
  //var menu_table = document.body.children['all'].children[0].children[1].children[0].children[2].children[0].children[0].children[0];
  if (debug > 0) alert('menu_table ' + menu_table);
  if (!menu_table) {
	  if (debug > 0) alert('menu_table not defined');
	  return;
	}
  if (debug>0) alert('menu3');
  for(var i=0;menu_table.children[i];i++){

     tLay=menu_table.children[i];
     if(tLay.tagName!="DIV" || tLay.id.charAt(0) != "m") continue;
     menu[tIdx]=new mItem(tLay.id);

     tLink = tArrow = null;
     for(var j=0;tLay.all[j];j++){
       var elm=tLay.all[j];
       if(elm.tagName=="A")tLink=elm;
       else if(elm.tagName=="IMG" && elm.src.indexOf(openImg)!=-1)tArrow=elm;
     }
     if (tLink){
       tLink.onmouseover=function(){hl(this);};
       tLink.onmouseout=function(){ll(this);};
       tLink.idx=tIdx;
       var path=tLink.pathname;
       if(!path)path="void(0)";
       if(path.indexOf(".htm")!=-1)path=path.substring(0,path.indexOf(".htm"));
       if(path.indexOf("?")!=-1)path=path.substring(0,path.indexOf("?"));
       menu[tIdx].href=path;

     }
     if(tArrow)menu[tIdx].arrow=tArrow;
     tIdx++;

  }
  for(i=0;i<menu.length;i++){
    if(menu[i].level==1){
      menu[i].moveTo(offsetTop);
      menu[i].show();
      offsetTop+=(menu[i].height);
    }
  }
  if(ns4 && offsetTop>window.innerHeight)window.document.height=offsetTop + bottomMargin;
  if(defaultOpen)openMenu();
  oImg=new Image(); oImg.src="/confirmations/images/menu/"+openImg;
  cImg=new Image(); cImg.src="/confirmations/images/menu/"+closeImg;
  o2Img=new Image(); o2Img.src="/confirmations/images/menu/l2_"+openImg;
  c2Img=new Image(); c2Img.src="/confirmations/images/menu/l2_"+closeImg;
  menuIsLoaded=true;
  setLoc();
}



