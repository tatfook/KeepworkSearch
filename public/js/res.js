/*md转化结果的详细信息-------------start*/
function showDetail(title,url){
    $('.modal-header > h4').html(title);
    if(url!='null'){
        $('.modal-content iframe').attr("src",url);
    }else{
        $('.modal-content iframe').attr("src",null);
    }
}
    
/*md转化结果的详细信息-------------end*/
$('#optionsRadios1').click(
    function(){
        $('.file1').show();
        $('.file2').hide();
    }
);
$('#optionsRadios2').click(
    function(){
        $('.file2').show();
        $('.file1').hide();
    }
);
var filename="";
var filesize=0;
var fileType="";
$('#isOCR').change(function(){
    if($('#isOCR').val()=="false"){
        $('#isOCR').val("true");
    }else{
        $('#isOCR').val("false");
    }
});
function getAll(filebyte){
    var fileKB=Math.round((filebyte*100)/1024)/100.00;
    var fileM=0;
    var filesize=0;
    if(fileKB>1000){
        fileM=Math.round((fileKB*100)/1024)/100.00;
        filesize=fileM+" M";
    }else{
        filesize=fileKB+" KB";
    }
    return filesize;
}
$('#file').change(function(){
    $('#history').addClass('hide');
    $('.allstart').removeClass('hide');
    $('.allcancel').removeClass('hide');
    $('.allhistory').addClass('hide');
    $('.fileinput-button').addClass('hide');
    var len=$('#file')[0]['files'].length;
    total=len;
    console.log(len);
    if(len==0){
        $('#upload').addClass('hide');
        $('.allstart').addClass('hide');
        $('.allcancel').addClass('hide');
        if($('.allhistory').hasClass('hide')){
            $('.allhistory').removeClass('hide');
        }
        if($('.fileinput-button').hasClass('hide')){
            $('.fileinput-button').removeClass('hide');
        }
    }else{
        var str='<table role="presentation" class="table table-striped"><thead><tr><th>文件信息</th><th>上传信息</th><th>操作/转换信息</th></tr></thead><tbody class="files">';
        for(var i=0;i<len;i++){
            console.log($('#file')[0]['files'][i]);
            if($('#file')[0]['files'][i]){
                filename=$('#file')[0]['files'][i]['name'];
                var filebyte=$('#file')[0]['files'][i]['size'];
                var filesize=getAll(filebyte);
                var arr=filename.split('.');
                fileType=arr[arr.length-1];
                str+='<tr class="template-upload fade in tr tr'+i+'"><td width="40%"><!--<input class="childFile" id="file'+i+'" type="file" name="file'+i+'" value="'+filename+'"/>--><p class="name">名称: '+filename+'</p><strong class="error text-danger"></strong><p class="size'+i+'" mybyte="'+filebyte+'">大小: '+filesize+'<span class="already'+i+'">&nbsp;&nbsp;已完成: 0KB</span></p></td><td width="20%"><p class="speed'+i+'">上传速度:   0kb/s</p><div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success progress'+i+'" style="width:0%;">0%</div></div></td><td width="40%"><button id="btn'+i+'" class="btn btn-primary start" onclick="upload('+i+')" style="margin-top:20px;margin-right:7px;"><i class="glyphicon glyphicon-upload"></i><span>开始</span></button><button id="ccl'+i+'" class="btn btn-warning cancel" style="margin-top:20px;"  onclick="cancelTr('+i+')"><i class="glyphicon glyphicon-ban-circle"></i><span>取消</span></button></td></tr>';
                if(fileType=="pdf"){
                    $('#ocrhide').removeClass('hide');
                }else{
                    $('#ocrhide').addClass('hide');
                }
            }
        }
        str+="</tbody></table>";
        $('#upload').removeClass('hide');   
        $('#upload table').remove();
        $('#upload .panel-body').append(str);
    }
    for(var i=0;i<len;i++){
        filename=$('#file')[0]['files'][i]['name'];
        var filebyte=$('#file')[0]['files'][i]['size'];
        var filesize=getAll(filebyte);

        var arr=filename.split('.');
        fileType=arr[arr.length-1];
        if(!(fileType=="doc"||fileType=="docx"||fileType=="ppt"||fileType=="pptx"||fileType=="pdf"||fileType=="html"||fileType=="htm"||fileType=="txt")){
            //alert("error");
            $('td > .size'+i).html('<div><span class="label label-danger">Error</span>&nbsp;请选择pdf/ppt/word/html/txt类型文件！</div>');
            $('#btn'+i).remove();
            $('.speed'+i).remove();
            $('.progress'+i).parent().remove();
        }

        //加一个对文件名的判断，不能包含空格
        var arr2=filename.split(' ');
        if(arr2.length>=2){
            $('td > .size'+i).html('<div><span class="label label-danger">Error</span>&nbsp;文件名中不能包含空格！</div>');
            $('#btn'+i).remove();
            $('.speed'+i).remove();
            $('.progress'+i).parent().remove();
        }
    }
});
function cancelTr(index){
    $('.tr'+index).remove();
    if($('.tr').length==0){
        $('#upload').addClass('hide');
        $('.allstart').addClass('hide');
        $('.allcancel').addClass('hide');
        if($('.allhistory').hasClass('hide')){
            $('.allhistory').removeClass('hide');
        }
        if($('.fileinput-button').hasClass('hide')){
            $('.fileinput-button').removeClass('hide');
        }
    }
}
function cancelAll(){
    var trs=$('.tr');
    for(var i=0;i<trs.length;i++){
        if($('.tr'+i+" .btn").hasClass('cancel')){
            $('.tr'+i).remove();
        }
    }
    for(var i=0;i<=total;i++){
        if($('tr button').hasClass('cancel')){
            $('.tr'+i).remove();
        }
    }
    if($('.tr').length==0){
        $('#upload').addClass('hide');
        $('.allstart').addClass('hide');
        $('.allcancel').addClass('hide');
        if($('.allhistory').hasClass('hide')){
            $('.allhistory').removeClass('hide');
        }
        if($('.fileinput-button').hasClass('hide')){
            $('.fileinput-button').removeClass('hide');
        }
    }
}