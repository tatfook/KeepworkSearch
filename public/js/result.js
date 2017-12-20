$(document).ready(function(){
    ajax();

    function ajax(){
        //alert(getUrlParms("p1"));
    	var inputGlobal = $.query.get("keyword");
        console.log(inputGlobal);
        $("#input").val(inputGlobal);
        var radioGlobal = $('input:radio:checked').val();
        var inputGlobal1 = $("#input").val();
        if(inputGlobal1 == ""){
            return;
        } /*else if(inputGlobal = "p1") {
            var url = window.location.search;
            var index = url.indexOf("=");
            var pos = url.substring(index+1);

            $.ajax({ 
                type: "POST",
                cache: true,
                url: "http://221.0.111.131:19001/Application/essearch",
                dataType: "json",
                data: { "keyword": pos, "page": 1, "flag": radioGlobal, "highlight": 1 },
                success: function(json) { 
                    console.log(json);
                    var totalGlobal = json.total;
                    var rateGlobal = json.took;
                    if(totalGlobal == 0) {
                        nothing(pos,rateGlobal);// pos需要改
                    }else {
                        trace(json);
                        search(totalGlobal,pos,radioGlobal);
                    }
                },
                error: function(msg) {
                    alert("出错了，重新加载");
                }
            });
        } */else {
            $.ajax({ 
                type: "POST",
                cache: true,
                url: "http://221.0.111.131:19001/Application/essearch",
                dataType: "json",
                data: { "keyword": inputGlobal, "page": 1, "flag": radioGlobal, "highlight": 1 },
                success: function(json) { 
                    console.log(json);
                    var totalGlobal = json.total;
                    var rateGlobal = json.took;
                    if(totalGlobal == 0) {
                	    nothing(inputGlobal,rateGlobal);
                    }else {
                	    trace(json);
                        search(totalGlobal,inputGlobal,radioGlobal);
                    }
                },
                error: function(msg) {
                	alert("出错了，重新加载");
                }
            });
        }
    }

     function getUrlParms(name){
   var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
   var r = window.location.search.substr(1).match(reg);
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
    	       "抱歉，没有找到与 \"" + "<span style=\"color:red\">" + content + "</span>" + "\" 相关的内容" +
    	    "</div>");
    }
        
    //分页功能
    function search(total,input,radio1) {
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
                    data: { "keyword": input, "page": num, "flag": radio1, "highlight": 1},
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
    	var radioLocal = $('input:radio:checked').val();
    	if(inputLocal == '') {
     		return false;
     	} else {
     		var bool = true;
     		$.ajax({ 
     	        type: "POST",
     	        cache: true,
     	        url: "http://221.0.111.131:19001/Application/essearch",
     	        dataType: "json",
     	        data: {"keyword": inputLocal, "page": 1, "flag": radioLocal, "highlight": 1},
     	        success: function(json) {
     	            var totalLocal = json.total;
     	            var rateLocal = json.took;
     	            if(totalLocal == 0){
     	            	 nothing(inputLocal,rateLocal);
     	            } else {
                       /* location.search="keyword=" + inputLocal; */
     	            	trace(json);
     	                search(totalLocal,inputLocal,radioLocal);
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
        	var radioLocal = $('input:radio:checked').val();
        	if(inputLocal == '') {
         		return false;
         	} else {
         		$.ajax({ 
         	        type: "POST",
         	        cache: true,
         	        url: "http://221.0.111.131:19001/Application/essearch",
         	        dataType: "json",
         	        data: {"keyword": inputLocal, "page": 1, "flag": radioLocal, "highlight": 1},
         	        success: function(json) {
         	            var totalLocal = json.total;
         	            var rateLocal = json.took;
         	            if(totalLocal == 0){
       	            	   nothing(inputLocal,rateLocal);
       	                } else {
       	                   trace(json);
       	                   search(totalLocal,inputLocal,radioLocal);
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
        url: "http://221.0.111.131:19001/Application/essearch",
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