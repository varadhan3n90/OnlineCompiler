<%-- 
    Document   : index
    Created on : Nov 13, 2013, 12:11:24 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Online Compiler</title>
        <script type="text/javascript" lang="javascript" src="js/jquery.js"></script>
        <script type="text/javascript" lang="javascript">
            function init(){
                getQIDs();
                getDefaultSnipetAndQuestion();
            }
            
            function getDefaultSnipetAndQuestion(){
                getQuestion(1);                
                getCodeTemplate(1,document.getElementById('lang').value);
            }
            
            function getQIDs(){
                $.get("/OnlineCompiler/QuestionID",function(data){                    
                    var totalQ = data;
                    var options = document.getElementById('qid').innerHTML;
                    for(i=1;i<=totalQ;i=i+1){
                        options = options + '<option>'+i+'</option>';
                    }
                    document.getElementById('qid').innerHTML = options;
                });
            }
            function getCodeTemplate(qid,lang){
                $.get("code/"+lang+"/"+qid,function(data){
                    document.getElementById('code').innerHTML = data;
                });
            }
            function getQuestion(qid){
                $.ajaxSetup({async:false});
                $.get('/OnlineCompiler/Question',{q:qid}).done(function(data){
                    var qAndL = data.split('@#@');
                    document.getElementById('questions').innerHTML = qAndL[0];
                    var langList = qAndL[1].split(',');
                    document.getElementById('lang').innerHTML = '';
                    for(i=0;i<langList.length;i=i+1){
                        document.getElementById('lang').innerHTML = document.getElementById('lang').innerHTML +'<option>'+langList[i]+'</option>';
                    }
                });
                $.ajaxSetup({async:true});
            }
            function postCode(){
                $('#submit').hide();            
                var codePart = document.getElementById('code').innerHTML;
                $.post('/OnlineCompiler/Compiler',{code:codePart}) .done(function( data ) {
                    $('#submit').show();
                    document.getElementById('status').innerHTML = data;
                });
            }
            function changeQuestion(){                
                getQuestion(document.getElementById('qid').value);
                getCodeTemplate(document.getElementById('qid').value,document.getElementById('lang').value);
            }
        </script>
    </head>
    <body onload="init();">
        <h1>Coding round</h1>
        <form method="post" action="">
            <p>Select question
                <select id="qid" onchange="changeQuestion();">
            </select></p>
            <h3>Question</h3>
            <p id="questions"></p>        
            <textarea name="code" id="code" rows="20" cols="100"></textarea>
            <p>Select language: <select id="lang" onchange="changeQuestion();"></select></p><p><input type="button" value="Compile"  onclick="postCode();" id="submit"></p>
            <div id="status"></div>
        </form>
    </body>
</html>
