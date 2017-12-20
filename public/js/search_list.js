$(document).ready(function(){
    ajax();
    function ajax(){
    	//var inputGlobal = $.query.get("keyword");
        //$("#input").val(inputGlobal);
        //var inputGlobal1 = $("#input").val();
        var p1 = getUrlParms("p1");
        console.log(p1);
        var p2 = getUrlParms("p2");
        console.log(p2);
        var p3 = getUrlParms("p3");
        console.log(p3);
        var p4 = getUrlParms("p4");
        console.log(p4);
        var p5 = getUrlParms("p5");
        console.log(p5);
        var p6 = getUrlParms("p6");
        console.log(p6);
        var p7 = getUrlParms("p7");
        console.log(p7);
        if(p2!="" && p3!="" && p4!="") {
            
            $("#input").val(p1 + " \"" + p2 + "\"" + " (" + p3 + ") " + "-" + "(" + p4 + ")");
        } else if(p2!="" && p3!="" && p4=="") {
            $("#input").val(p1 + " \"" + p2 + "\"" + " (" + p3 + ") ");
        } else if(p2!="" && p3=="" && p4!="") {
            $("#input").val(p1 + " \"" + p2 + "\"" + "-" + "(" + p4 + ")");
        } else if(p2=="" && p3!="" && p4!="") {
            $("#input").val(p1 + " (" + p3 + ") " + "-" + "(" + p4 + ")");
        } else if(p2=="" && p3=="" && p4!="") {
            $("#input").val(p1 + " \"" + "(" + p4 + ")");
        } else if(p2=="" && p3!="" && p4=="") {
            $("#input").val(p1 + " (" + p3 + ") ");
        } else if(p2!="" && p3=="" && p4=="") {
            $("#input").val(p1 + " \"" + p2 + "\"");
        } else if(p2=="" && p3=="" && p4=="") {
             $("#input").val(p1);
        }
        $.ajax({ 
            type: "POST",
            cache: true,
            url: "http://221.0.111.131:19001/Application/bkASearch",
            dataType: "json",
            data: { "page": 1, "p1": p1, "p2": p2, "p3": p3, "p4": p4, "p5": p5, "p6": p6, "p7": p7},
            success: function(json) { 
                console.log(json);
                var totalGlobal = json.total;
                var rateGlobal = json.took;
                if(totalGlobal == 0) {
                    var inputGlobal = p1 + "  " + p2 + "  " + p3 + "  " + p4 + "  " + p5 + "  " + p6 + "  " + p7;
            	    nothing(inputGlobal,rateGlobal);
                } else {
            	   trace(json);
                   search(totalGlobal,p1,p2,p3,p4,p5,p6,p7);
                }
            },
            error: function(msg) {
            	alert("出错了，重新加载");
            }
        });
    }

    function getUrlParms(name){
       var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
       var url = decodeURI(window.location.search);
       var index = url.indexOf("=");
       var pos = url.substring(index+1);
       var pos1= "?" + pos;
       var r = pos1.substr(1).match(reg);
       if(r!=null)
       return unescape(r[2]);
       return null;
    }

    function trace(json) {
    	$(".digital").html("");
       	$(".digital").append(json.total + " 命中");
       	$(".second").html("");
       	$(".second").append("耗时 " + json.took + " 秒");
        $("#mainbody").html("");
        var data = 1;
        var d = new Date();
        var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
        $.each(json.data.list,function(i, item) {
            $("#mainbody").append(
                "<div class='list'><div class='title'><a onclick='getDetail(\""+item.iid+"\")' data-toggle='modal' data-target='#myModal'>"+item.title+"</a></div><div class='content'>" + item.content + "</div><a href='javascript:void(0);' class='link1'>" + "www.confuchina/ - " + str + "</a></div>");
        });
    }

    //没有搜索到内容
    function nothing(content,rate) {
    	$(".digital").html("");
    	$(".digital").append(0 + " 命中");
    	$(".second").html("");
    	$(".second").append("耗时 " + rate + " 秒");
    	$("#mainbody").html("");
    	$("#pagination").html("");
    	$("#mainbody").append(
    	    "<div class=\"list\">" +
    	       "抱歉，没有找到与 \"" + "<span style=\"color:red\">" + content + "</span>" + "\" 相关的百科搜索内容" +
    	    "</div>");
    }

    //分页功能
    function search(total,p1,p2,p3,p4,p5,p6,p7) {
        $.jqPaginator('#pagination', {
            totalCounts: total,//分页的总条目数
            visiblePages: 7,
            currentPage: 1,
            pageSize: 10,
            wrapper:'<ul class="pagination"></ul>',
           /* first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',*/
            prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
            next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
           /* last: '<li class="last"><a href="javascript:void(0);">末页</a></li>',*/
            page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
            onPageChange: function(num,type) {
                //下面是ajax请求
            	var bool = true;
            	if(type == "init") {
            		return;
            	}
                $.ajax({
                    type: "POST",
                    cache: true,
                    url:"http://221.0.111.131:19001/Application/bkASearch",
                    dataType: "json",                    
                    data: { "p1": p1, "p2": p2, "p3": p3, "p4": p4, "p5": p5, "p6": p6, "p7": p7, "page": num},
                    success: function(json) {
                    	trace(json);
                    },
                    error: function(msg) {
                        alert("出错了，重新加载");
                    }
                });//ajax加载结束   
           }//onPageChange结束
      });//jqpaginator结束
    }//search结束

    //分页功能
    function search1(total,input) {
        $.jqPaginator('#pagination', {
            totalCounts: total,//分页的总条目数
            visiblePages: 7,
            currentPage: 1,
            pageSize: 10,
            wrapper:'<ul class="pagination"></ul>',
           /* first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',*/
            prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
            next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
            /*last: '<li class="last"><a href="javascript:void(0);">末页</a></li>',*/
            page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
            onPageChange: function(num,type) {
                //下面是ajax请求
                var bool = true;
                if(type == "init") {
                    return;
                }
                $.ajax({
                    type: "POST",
                    cache: true,
                    url:"http://221.0.111.131:19001/Application/essearch",
                    dataType: "json",                    
                    data: { "keyword": input, "page": num, "flag": 0, "highlight": 1},
                    success: function(json) {
                        trace(json);
                    },
                    error: function(msg) {
                        alert("出错了，重新加载");
                    }
                });//ajax加载结束   
            }//onPageChange结束
        });//jqpaginator结束
    }//search结束

    //点击搜索
    $("#search-btn").click(function(){
    	var inputLocal = $("#input").val();
    	if(inputLocal == '') {
     		return false;
     	} else {
     		var bool = true;
     		$.ajax({ 
     	        type: "POST",
     	        cache: true,
     	        url: "http://221.0.111.131:19001/Application/baikesearch",
     	        dataType: "json",
     	        data: {"keyword": inputLocal, "page": 1, "flag": 0, "highlight": 1},
     	        success: function(json) {
     	            var totalLocal = json.total;
     	            var rateLocal = json.took;
     	            if(totalLocal == 0){
     	            	 nothing(inputLocal,rateLocal);
     	            } else {
     	            	 trace(json);
     	                 search1(totalLocal,inputLocal);
     	            }
     	        },
     	       error: function(msg) {
                   alert("出错了，重新加载");
               }
     	    });
       }
    });//点击事件

    $("#input").keydown(function(){
    	if(event.which == "13"){
    		var inputLocal = $("#input").val();
        	if(inputLocal == '') {
         		return false;
         	} else {
         		$.ajax({ 
         	        type: "POST",
         	        cache: true,
         	        url: "http://221.0.111.131:19001/Application/baikesearch",
         	        dataType: "json",
         	        data: {"keyword": inputLocal, "page": 1, "flag": 0, "highlight": 1},
         	        success: function(json) {
         	            var totalLocal = json.total;
         	            var rateLocal = json.took;
         	            if(totalLocal == 0){
       	            	   nothing(inputLocal,rateLocal);
       	                } else {
       	                   trace(json);
       	                   search1(totalLocal,inputLocal);
       	                }
         	        },
         	        error: function(msg) {
                       alert("出错了，重新加载");
                    }
         	    });
           }
    	}
    });//回车事件  
});//ready结束
    
//查看详情
function getDetail(id){
    $.ajax({ 
        type: "POST",
        cache: true,
        url: "http://221.0.111.131:19001/Application/baikesearch",
        dataType: "json",
        data: {"keyword": id, "page": 1, "flag": 2},
        success: function(json) { 
            $('.modal-content h3').html(json.data.title);
            $('.modal-content p').html(json.data.content);
        },
        error: function(msg) {
            alert("出错了，重新加载");
        }
    });
}   