<!-- licence1: Apache-2.0 licence2: AGPL-3.0  from1: japidoc from2:doc-apis originated -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>${controller.description}</title>
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <link rel="stylesheet" href="../css/font-awesome.min.css">
    <link rel="stylesheet" href="../css/prettify.min.css">
    <link rel="stylesheet" href="../css/style.css">
</head>
<body onload="PR.prettyPrint()">
<nav class="navbar">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="../index.html">
                <img src="../img/logo.png" style="height: 100%">
            </a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="https://www.doc-apis.com" target="_blank">Home</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${currentApiVersion}<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <#list apiVersionList as version>
                            <#if version != currentApiVersion>
                                <li><a href="../${version}/index.html">${version}</a></li>
                            </#if>
                        </#list>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
<div class="book with-summary">
    <div class="book-summary">
        <#include "api-common-catalog.html.ftl"/>
    </div>
    <div class="book-body">
        <div class="body-inner">
            <div class="book-header">
                <div class="d-flex justify-content-between">
                    <a class="header-menu toggle-catalog" href="javascript:void(0)"><i
                                class="glyphicon glyphicon-align-justify"></i> ${i18n.getMessage('catalog')}</a>
                </div>
            </div>
            <div class="page-wrapper">
                <div class="page-inner">
                    <div class="global-config-wrap">
                        <div class="server-wrap">
                            <label>Servers</label>
                            <input id="serversUrl" class="input-style search-box" type="text" value="http://localhost:8080" />
                        </div>
                        <p class="gbtn gbtn-green">Global header <i class="fa fa-gear" aria-hidden="true"></i></p>
                    </div>

                    <div class="action-list">
                        <#list controller.requestNodes as reqNode>
                            <div class="action-item" id="${reqNode_index}">
                                <#assign requestNode = reqNode/>
                                <#if reqNode.lastRequestNode?? && reqNode.changeFlag == 2>
                                    <ul class="nav nav-tabs" role="tablist">
                                        <li role="presentation" class="active"><a href="#current" aria-controls="current" role="tab" data-toggle="tab">${i18n.getMessage('currentVersion')}</a></li>
                                        <li role="presentation"><a href="#last" aria-controls="last" role="tab" data-toggle="tab">${i18n.getMessage('lastVersion')}</a></li>
                                    </ul>
                                    <div class="tab-content">
                                        <div role="tabpanel" class="tab-pane active" id="current">
                                            <#include "api-request-node.html.ftl"/>
                                        </div>
                                        <div role="tabpanel" class="tab-pane" id="last">
                                            <#assign requestNode = reqNode.lastRequestNode/>
                                            <#include "api-request-node.html.ftl"/>
                                        </div>
                                    </div>
                                <#else>
                                    <#include "api-request-node.html.ftl"/>
                                </#if>
                            </div>
                            <hr>
                        </#list>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 水印 -->
    <div class="watermark"></div>
</div>

<!-- 全局配置 -->
<div class="global-config-dialog">
    <div class="global-config-header">
        <label>Global header</label>
        <i class="fa fa-close close-dialog" aria-hidden="true"></i>
    </div>
    <div class="global-config-content">
        <ul id="list">
            <li>
                <p>Key</p>
                <p>Value</p>
                <i style="width: 14px;"></i>
            </li>
            <li>
                <p><input class="input-style search-box" type="text" value="" placeholder="Key" /></p>
                <p><input class="input-style search-box" type="text" value="" placeholder="Value" /></p>
                <i class="fa fa-minus-square" aria-hidden="true"></i>
            </li>
        </ul>
        <span id="addGlobalBtn"><i class="fa fa-plus-square-o mr10" aria-hidden="true" title="add"></i>Add</span>
    </div>
    <div class="global-config-bottom">
        <p class="gbtn gbtn-green-ghost">save</p>
    </div>
</div>

<!-- 内页配置 -->
<div class="inner-config-dialog">
    <div class="global-config-header">
        <label>Header</label>
        <i class="fa fa-close close-dialog" aria-hidden="true"></i>
    </div>
    <div class="global-config-content">
        <ul id="listInner">
            <li>
                <p>Key</p>
                <p>Value</p>
                <i style="width: 14px;"></i>
            </li>
            <li>
                <p><input class="input-style search-box" type="text" value="" placeholder="Key" /></p>
                <p><input class="input-style search-box" type="text" value="" placeholder="Value" /></p>
                <i class="fa fa-minus-square" aria-hidden="true"></i>
            </li>
        </ul>
        <span id="addBtn"><i class="fa fa-plus-square-o mr10" aria-hidden="true" title="add"></i>Add</span>
    </div>
    <div class="global-config-bottom">
        <p class="gbtn gbtn-green-ghost">save</p>
    </div>
</div>
<!-- 遮罩层 -->
<div class="overlay"></div>
<!-- 提示框 -->
<div id="tips" title="提示信息"></div>

<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/autocomplete.jquery.min.js"></script>
<script src="../js/prettify.min.js"></script>

<script>

    // 动态生成水印
    const watermarkContainer = document.querySelector('.watermark');

    for (let i = 0; i < 1000; i++) { // 根据需要调整数量
        const div = document.createElement('div');
        div.textContent = 'doc-apis';
        watermarkContainer.appendChild(div);
    }

    // 从本地存储中获取并设置 serversUrl 的初始值,没有初始值的话取默认的存
    if (localStorage.getItem('serversUrl')) {
        $('#serversUrl').val(localStorage.getItem('serversUrl'));
    }else{
        localStorage.setItem('serversUrl', $('#serversUrl').val());
    }
    // 监听 input 变化并实时更新本地存储serversUrl
    $('#serversUrl').on('input', function() {
        var inputValue = $(this).val();
        localStorage.setItem('serversUrl', inputValue);
    });

    // 从本地存储加载数据并渲染
    function loadData() {
        $('#list').find('li:not(:first)').remove();// 清空除了标题行之外的所有行
        const data = JSON.parse(localStorage.getItem('globalHeader')) || [];
        data.forEach(item => {
            addGlobalListItem(item.key, item.value);
        });
    }
    // 添加新行
    <#noparse>
    function addGlobalListItem(key = '', value = '') {
        $('#list').append(`
            <li>
                <p><input class="input-style search-box" type="text" value="${key}" placeholder="Key" /></p>
                <p><input class="input-style search-box" type="text" value="${value}" placeholder="Value" /></p>
              <i class="fa fa-minus-square" aria-hidden="true"></i>
            </li>
        `);
    }

    // 保存数据到本地存储
    function saveData() {
        const data = [];
        $('#list li').each(function(index) {
            if (index !== 0) { // 跳过标题行
                const key = $(this).find('input').eq(0).val();
                const value = $(this).find('input').eq(1).val();
                if (key) {
                    data.push({ key: key, value: value });
                }
            }
        });
        localStorage.setItem('globalHeader', JSON.stringify(data));
    }
    // 保存输入框的变化到本地存储
    $(".global-config-bottom p").click(function() {
        saveData();
        $(".overlay").fadeOut(300);
        $(".global-config-dialog").fadeOut(300);
    });

    $(".global-config-wrap p").click(function() {
        loadData();
        $(".overlay").fadeIn(300);
        $(".global-config-dialog").fadeIn(300);
    });
    $(".close-dialog").click(function() {
        $(".overlay").fadeOut(300);
        $(".global-config-dialog").fadeOut(300);
    });
    // 点击添加按钮增加一行
    $("#addGlobalBtn").click(function() {
        $("#list").append('<li><p><input class="input-style search-box" type="text" value="" placeholder="Key" /></p><p><input class="input-style search-box" type="text" value="" placeholder="Value" /></p><i class="fa fa-minus-square" aria-hidden="true"></i></li>');
    });

    // 点击删除图标删除行
    $("#list").on("click", ".fa-minus-square", function() {
        $(this).parent().remove();
    });
    </#noparse>

    var selectedParentId = null;

    // 生成表单的方法
    function generateForm(data) {
        var form = $('<form id="dynamicForm"></form>');

        for (var key in data) {
            if (data.hasOwnProperty(key)) {
                var value = data[key];

                var checkbox = $('<input>').attr({
                    type: 'checkbox',
                    class: 'data-checkbox',
                    checked: true
                });
                var label = $('<label></label>').text(key);
                var input = $('<input>').attr({
                    type: 'text',
                    name: key,
                    class: 'input-style',
                    value: value
                });

                var formGroup = $('<div class="form-group-wrap"></div>').append(checkbox).append(label).append(input);
                form.append(formGroup);
            }
        }

        // 添加两个空白输入框用于新键值对
        var emptyInputGroup = createEmptyInputGroup();
        form.append(emptyInputGroup);

        return form;
    }
    // 创建空白输入框的组
    function createEmptyInputGroup() {
        var checkbox = $('<input>').attr({
            type: 'checkbox',
            class: 'data-checkbox',
            checked: false
        });

        var inputKey = $('<input>').attr({
            type: 'text',
            name: 'newKey',
            class: 'input-style f-col-black'
        });

        var inputValue = $('<input>').attr({
            type: 'text',
            name: 'newValue',
            class: 'input-style'
        });

        var formGroup = $('<div class="form-group-wrap new-input-group"></div>').append(checkbox).append(inputKey).append(inputValue);

        // 绑定输入事件
        inputKey.on('input', handleNewInput);
        inputValue.on('input', handleNewInput);

        return formGroup;
    }
    // 将 b 的元素合并到 a 中
    function mergeArrays(a, b) {
        if (!Array.isArray(a)) {
            a = [];
        }
        if (!Array.isArray(b)) {
            b = [];
        }
        var aMap = {};
        // 将 a 转换为 Map 结构，方便查找
        a.forEach(function (item) {
            aMap[item.key] = item;
        });
        // 遍历 b，根据 key 更新或插入到 aMap
        b.forEach(function (item) {
            aMap[item.key] = item;
        });
        // 将 aMap 转换回数组形式
        var result = Object.values(aMap);
        return result;
    }
    // 将数组转换为请求头对象
    function arrayToHeaders(array) {
        var headers = {};
        array.forEach(function (item) {
            headers[item.key] = item.value;
        });
        return headers;
    }
    // 添加新行
    <#noparse>
    function addListItem(key = '', value = '') {
        $('#listInner').append(`
                <li>
                    <p><input class="input-style search-box" type="text" value="${key}" placeholder="Key" /></p>
                    <p><input class="input-style search-box" type="text" value="${value}" placeholder="Value" /></p>
                    <i class="fa fa-minus-square" aria-hidden="true"></i>
                </li>
            `);
    }
    </#noparse>
    $(".inner-header").click(function () {
        let pID = $(this).closest('.action-item').attr('id');
        selectedParentId = pID;
        loadDataInner(pID);
        $(".overlay").fadeIn(300);
        $(".inner-config-dialog").fadeIn(300);
    });
    $(".close-dialog").click(function () {
        $(".overlay").fadeOut(300);
        $(".inner-config-dialog").fadeOut(300);
    });
    // 点击添加按钮增加一行
    $("#addBtn").click(function () {
        $("#listInner").append('<li><p><input class="input-style search-box" type="text" value="" placeholder="Key" /></p><p><input class="input-style search-box" type="text" value="" placeholder="Value" /></p><i class="fa fa-minus-square" aria-hidden="true"></i></li>');
    });
    // 点击删除图标删除行
    $("#listInner").on("click", ".fa-minus-square", function () {
        $(this).parent().remove();
    });
    // 保存输入框的变化到本地存储
    $(".global-config-bottom p").click(function () {
        let pID = selectedParentId;
        saveDataInner(pID);
        $(".overlay").fadeOut(300);
        $(".inner-config-dialog").fadeOut(300);
    });
    // 保存数据到本地存储
    function saveDataInner(idName) {
        const data = [];
        $('#listInner li').each(function (index) {
            if (index !== 0) { // 跳过标题行
                const key = $(this).find('input').eq(0).val();
                const value = $(this).find('input').eq(1).val();
                if (key) {
                    data.push({ key: key, value: value });
                }
            }
        });
        localStorage.setItem(idName, JSON.stringify(data));
    }
    // 从本地存储加载数据并渲染
    function loadDataInner(idName) {
        $('#listInner').find('li:not(:first)').remove();// 清空除了标题行之外的所有行
        const data = JSON.parse(localStorage.getItem(idName)) || [];
        data.forEach(item => {
            addListItem(item.key, item.value);
        });
    }
    // 处理新输入事件
    // 如果两个输入框中任一个已有值，就不会再新增一行，只有当前行的两个输入框都为空时才新增新行
    function handleNewInput() {
        var lastEmptyGroup = $('#dynamicForm').find('.new-input-group').last();
        var emptyKeyInput = lastEmptyGroup.find('input[name="newKey"]');
        var emptyValueInput = lastEmptyGroup.find('input[name="newValue"]');
        var checkbox = lastEmptyGroup.find('input[type="checkbox"]');

        if ((emptyKeyInput.val().trim() === '' && emptyValueInput.val().trim() !== '')
            || (emptyKeyInput.val().trim() !== '' && emptyValueInput.val().trim() === '')) {
            // 将当前行的复选框设置为选中
            checkbox.prop('checked', true);
            var newInputGroup = createEmptyInputGroup();
            $('#dynamicForm').append(newInputGroup);
        }
    };
    //获取表格内的数据组装成json格式
    function formatTableData(pID) {
        var result = {};
        // 遍历表格的每一行（跳过表头）
        $( pID + ' tr').each(function (index) {
            if (index === 0) return; // 跳过表头

            var paramName = $(this).find('td').eq(0).text();
            var type = $(this).find('td').eq(1).text();

            if (paramName && type) {
                result[paramName] = type;
            }
        });
        return result
    };

    //debug
    $(".debug-btn-wrap a").on("click", function () {
        let pID = $(this).closest('.action-item').attr('id');
        $("#" + pID + " .debug-area").toggleClass("show-area");

        let isJson = $("#" + pID + " .badge").text();
        $("#" + pID + ' .params-form').empty();
        $("#" + pID + ' .debug-params').hide();
        $("#" + pID + ' .debug-textarea').hide();
        $("#" + pID + ' .responses-area').empty();
        $("#" + pID + ' .curl-area').empty();
        $("#" + pID + ' .responses-wrap').hide();

        if (isJson == "application/x-www-form-urlencoded") { //表单
            // 将表单添加到容器中
            let paramsData = formatTableData("#" + pID + " .table");
            $("#" + pID + ' .debug-params').show();
            $("#" + pID + ' .params-form').append(generateForm(paramsData));
        } else { //文本域
            $("#" + pID + ' .debug-textarea').show();
            let paramsData = JSON.parse($("#" + pID + " .in-params").text());
            $("#" + pID + ' .debug-textarea').val(JSON.stringify(paramsData));
        }
        $("#" + pID + ' .debug-btn-wrap span').text(function () {
            return $(this).text() == 'debug' ? 'debug close' : 'debug';
        })
    });

    //生成curl方法
    <#noparse>
    function generateCurlCommand(url, method = 'GET', headers = {}, data = {},pID,type) {
        let curlCommand;
        if (method === 'GET' || method === 'DELETE'){
            const queryString = new URLSearchParams(data).toString();
            curlCommand = `curl -X ${method} "${url}?${queryString}"`;
        }else{
            curlCommand = `curl -X ${method} "${url}"`;
        }


        // 处理请求头
        curlCommand += ` -H "Content-Type: ${type}"`;
        Object.keys(headers).forEach(header => {
            curlCommand += ` -H "${header}: ${headers[header]}"`;
        });

        // 处理数据（GET请求除外）
        if ((method === 'PUT' || method === 'POST') && Object.keys(data).length) {
            curlCommand += ` -d '${JSON.stringify(data)}'`;
        }
        $("#" + pID + ' .curl-area').text(curlCommand);
    }
    </#noparse>

    // 封装的通用 AJAX 请求方法
    function makeRequest(url, method, headers, data, pID, type) {
        return $.ajax({
            url: url,
            method: method,
            headers: headers,
            data: method === 'GET' || method === 'DELETE' ? data : JSON.stringify(data),
            contentType: type,//"application/x-www-form-urlencoded",
            success: function (response) {
                console.log('Success:', response);
                $("#" + pID + ' #loadingSpinner').hide();
                $("#" + pID + ' .responses-wrap').fadeIn();
                $("#" + pID + ' .response-code').removeClass('f-col-red').addClass('f-col-green').text('200')
                $("#" + pID + ' .responses-area').text(JSON.stringify(response, null, 2));
            },
            error: function (error, status) {
                console.error('Error:error', error, status);
                $("#" + pID + ' #loadingSpinner').hide();
                $("#" + pID + ' .responses-wrap').fadeIn();
                $("#" + pID + ' .response-code').removeClass('f-col-green').addClass('f-col-red').text(error.status);
                if (error.responseJSON) {
                    $("#" + pID + ' .responses-area').text(JSON.stringify(error.responseJSON.error, null, 2));
                } else {
                    $("#" + pID + ' .responses-area').text('Failed！');
                }
            }
        });
    }

    // 执行请求
    $(".execute").on("click", function () {
        let pID = $(this).closest('.action-item').attr('id');
        $("#" + pID + ' .responses-area').empty();
        $("#" + pID + ' .responses-wrap').fadeOut();
        let gUrl = localStorage.getItem('serversUrl');
        let iUrl = $("#" + pID + " .innerUrl").text()
        let url = gUrl + iUrl;
        var method = $("#" + pID + " .label").text();
        var data = $("#" + pID + " .in-params").text();
        var type = $("#" + pID + " .badge").text();

        let globalHeader = JSON.parse(localStorage.getItem('globalHeader')) || {};
        let innerHeader = JSON.parse(localStorage.getItem(pID)) || {};

        var headers = arrayToHeaders(mergeArrays(globalHeader, innerHeader));
        // 显示加载动画
        $("#" + pID + ' #loadingSpinner').show();

        var formData = {};
        if (type != "application/x-www-form-urlencoded") {
            $("#" + pID + ' .debug-params').hide();
            try {
                formData = JSON.parse($("#" + pID + ' .debug-textarea').val());
            } catch (e) {
                console.log(e);
                $("#tips").text("请检查入参格式").fadeIn().delay(3000).fadeOut();
            }

        } else {
            $('#dynamicForm').find('.form-group-wrap').each(function () {
                var checkbox = $(this).find('.data-checkbox');
                if (checkbox.is(':checked')) {
                    var inputs = $(this).find('input[type="text"]');
                    if (inputs.length === 2) {
                        // 新增键值对的处理
                        var key = inputs.eq(0).val().trim();
                        var value = inputs.eq(1).val().trim();
                        if (key) {
                            formData[key] = value;
                        }
                    } else {
                        // 现有键值对的处理
                        formData[inputs.attr('name')] = inputs.val();
                    }
                }
            });
        }

        makeRequest(url, method, headers, formData, pID, type);
        generateCurlCommand(url, method, headers,formData, pID, type)
    });

    //点击复制
    $('.curl-wrap em').on("click", function () {
        let textArea = $(this).closest('.curl-wrap').find('.prettyprint').text();
        // 创建一个临时的 textarea 元素
        let tempTextArea = $('<textarea>');
        tempTextArea.val(textArea).appendTo('body').select();

        try {
            let successful = document.execCommand('copy');
            let msg = successful ? '复制成功' : '复制失败';
            $("#tips").text(msg).fadeIn().delay(3000).fadeOut();
        } catch (err) {
            console.error('复制失败', err);
            $("#tips").text("复制失败").fadeIn().delay(3000).fadeOut();
        }
        // 移除临时的 textarea 元素
        tempTextArea.remove();
    });

    var search_source_data = [
        <#list controllerNodeList as ctrolNode>
        <#list ctrolNode.requestNodes as reqNode>
        {name: '${ctrolNode.description}.${(reqNode.description)!''}', url: '${reqNode.codeFileUrl}'},
        </#list>
        </#list>
    ];

    $('.toggle-catalog').click(function () {
        $('.book').toggleClass('with-summary');
    });

    $('#inputSearch').autocomplete({ hint: false }, [
        {
            source: function (query, callback) {
                var result = [];
                for (var i = 0; i !== search_source_data.length; i++) {
                    if (search_source_data[i].name.indexOf(query) !== -1) {
                        result.push(search_source_data[i]);
                    }
                }
                callback(result);
            },
            displayKey: 'name',
            templates: {
                suggestion: function (suggestion) {
                    return suggestion.name;
                }
            }
        }
    ]).on('autocomplete:selected', function (event, suggestion, dataset, context) {
        self.location = suggestion.url;
    });
</script>

</body>
</html>