function search() {
	var params = $("#form").serialize();
	console.log(params);
	jump(params);
}

//跳转页面
function jump(params) {
    var url = "search_list.html?keyword=" + params;
    window.location.href = url;
}

