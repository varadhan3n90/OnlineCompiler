<%-- 
    Document   : index
    Created on : Nov 13, 2013, 12:11:24 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%@page import="javax.servlet.http.*;"%>
    <% 
    if((!session.isNew())&&session.getAttribute("user")==null)
    {
    RequestDispatcher rd=request.getRequestDispatcher("/index.jsp");
    rd.forward(request, response);
    }    
    %>  
    
    <head>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Online Compiler</title>
        <style type="text/css">
        .warning {
                color: #FF0000;
        }
        </style>
        <script type="text/javascript" lang="javascript" src="js/jquery.js"></script>
        <script type="text/javascript" lang="javascript">
            $.ajaxSetup({async:false});
            var codePadEditor;
            function init(){
                $('#loader').hide();
                $('#tick').hide();
                //$.ajaxSetup({async:false});
                getQIDs();
                getDefaultSnipetAndQuestion();
                //$.ajaxSetup({async:true});
                var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
                  mode: "text/x-csrc",
                  styleActiveLine: true,
                  lineNumbers: true,
                  lineWrapping: true
                });
                codePadEditor = editor;
            }
            function checkIsCompleted(){ 
                //alert('ck complete')
                var question = document.getElementById('qid').value;
                $.get('/OnlineCompiler/CheckCompleted',{qno:question}).done(function(data){                    
                    if(data.indexOf("true")>=0){
                        document.getElementById('status').innerHTML = "Completed";
                        $('#tick').show();
                    }
                });
                
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
                    document.getElementById('code').value = data;
                    checkIsCompleted();
                });
                
            }
            function getQuestion(qid){
                
                //$.ajaxSetup({async:false});
                $.get('/OnlineCompiler/Question',{q:qid}).done(function(data){
                    var qAndL = data.split('@#@');
                    document.getElementById('questions').innerHTML = qAndL[0];
                    var langList = qAndL[1].split(',');
                    document.getElementById('lang').innerHTML = '';
                    for(i=0;i<langList.length;i=i+1){
                        document.getElementById('lang').innerHTML = document.getElementById('lang').innerHTML +'<option>'+langList[i]+'</option>';
                    }
                });
                
                //$.ajaxSetup({async:true});
                
            }
            function postCode(){
                $('#submit').hide();
                $('#loader').show();
                codePadEditor.save();
                document.getElementById('status').innerHTML = "";
                var codePart = document.getElementById('code').value;
                //alert(codePart);
                var lang = document.getElementById('lang').value;
                var ques = document.getElementById('qid').value;
                document.getElementById('lang').setAttribute("disabled","true");
                document.getElementById('qid').setAttribute("disabled","true");
                $.post('/OnlineCompiler/Compiler',{code:codePart,language:lang,qno:ques}) .done(function( data ) {
                    $('#submit').show();
                    document.getElementById('status').innerHTML = data;
                    document.getElementById('lang').removeAttribute("disabled");
                    document.getElementById('qid').removeAttribute("disabled");
                    $('#loader').hide();
                });
                
            }
            function changeQuestion(){
                codePadEditor.toTextArea();
                getQuestion(document.getElementById('qid').value);
                getCodeTemplate(document.getElementById('qid').value,document.getElementById('lang').value);                
                codePadEditor = CodeMirror.fromTextArea(document.getElementById("code"), {
                  mode: "text/x-csrc",
                  styleActiveLine: true,
                  lineNumbers: true,
                  lineWrapping: true
                });
                $('#tick').hide();
                document.getElementById('status').innerHTML = "";
            }
            
            function saveCodeToDB(){
                codePadEditor.save();
                var codePart = document.getElementById('code').value;
                alert('Saving code to database');
                var qid = document.getElementById('qid').value;                
                $.get('/OnlineCompiler/SaveUserCode',{code:codePart,qid:qid,load:0}) .done(function( data ) {
                    alert(data);
                });
            }
            
            function loadCodeFromDB(){
                codePadEditor.toTextArea();
                var qid = document.getElementById('qid').value;
                $.get('/OnlineCompiler/SaveUserCode',{qid:qid,load:1}) .done(function( data ) {
                    document.getElementById('code').value=data;                    
                });
                codePadEditor = CodeMirror.fromTextArea(document.getElementById("code"), {
                  mode: "text/x-csrc",
                  styleActiveLine: true,
                  lineNumbers: true,
                  lineWrapping: true
                });
                
            }
        </script>
        
        <script src="js/codemirror/lib/codemirror.js"></script>
        <link rel="stylesheet" href="js/codemirror/lib/codemirror.css">
        <script src="js/codemirror/mode/javascript/javascript.js"></script>
        <script src="js/codemirror/mode/clike/clike.js"></script>
        <script src="js/codemirror/addon/selection/active-line.js"></script>        
        <link rel=stylesheet href="js/codemirror/doc/docs.css">
        
    </head>
    
    <body onload="init();">
        
        <div align="right"><h4><a href="/OnlineCompiler/Logout">Logout</a></h4></div>
        <h1>Coding round</h1>
        <form method="post" action="">
           
            <p>Select question
                <select id="qid" onchange="changeQuestion();">
            </select></p>
            <h3>Question</h3>
            <p id="questions"></p> 
            <textarea name="code" id="code" rows="20" cols="100"></textarea>
            <p>Select language: <select id="lang" onchange="changeQuestion();"></select></p><p><input type="button" value="Compile"  onclick="postCode();" id="submit"><input type="button" value="Save code" onclick="saveCodeToDB();"><input type="button" onclick="loadCodeFromDB();" value="Load Saved Code"></p>            
            <p id="loader"><img src="/OnlineCompiler/images/loading.gif" >Compiling and executing.. Please wait..</p>
            <p><img src="/OnlineCompiler/images/completed.png" id="tick">
            <div id="status"></div></p>
        <p> </p>
        <p class="warning"> Your main function should always return 0 for successful execution</p>
        </form>
    </body>
</html>
