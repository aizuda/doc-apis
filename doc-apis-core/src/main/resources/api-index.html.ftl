<!-- licence1: Apache-2.0 licence2: AGPL-3.0  from1: japidoc from2:doc-apis originated -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>${projectName}API Documentation</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/prettify.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body onload="PR.prettyPrint()">
<nav class="navbar">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="index.html">
                <img src="img/logo.png" style="height: 100%">
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
                    <div class="main-content">
                        <div class="ad-img">
                            <img src="https://iknow.hs.net/cedee01f-024f-46c8-ba30-3e88de690b55.png">
                            <span class="ad-text">本广告位招募赞助商  Mail:xpc@dromara.org</span>
                        </div>
                    </div>

                </div>
            </div>

            <!-- 密级 -->
            <div class="secret-level">${i18n.getMessage('classificationLevel')}:${classificationLevel}</div>

            <!-- 水印 -->
            <div class="watermark"></div>
        </div>
    </div>
</div>

<script src="./js/jquery.min.js"></script>
<script src="./js/bootstrap.min.js"></script>
<script src="./js/autocomplete.jquery.min.js"></script>
<script src="./js/prettify.min.js"></script>

<script>
    // 动态生成水印
    const watermarkContainer = document.querySelector('.watermark');

    for (let i = 0; i < 1000; i++) { // 根据需要调整数量
        const div = document.createElement('div');
        div.textContent = '${watermark!''}';
        watermarkContainer.appendChild(div);
    }

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

    $('#inputSearch').autocomplete({hint: false}, [
        {
            source: function (query, callback) {
                var result = [];
                for(var i = 0; i !== search_source_data.length; i++){
                    if(search_source_data[i].name.indexOf(query) !== -1){
                        var item = search_source_data[i];
                        result.push(item);
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

    let links = document.querySelectorAll('.in a');
    links.forEach(link => {
        link.addEventListener('click', function (event) {
            const storedValue = localStorage.getItem('openDocApi');
            const now = new Date().getTime();
            if (storedValue) {
                const storedObject = JSON.parse(storedValue);
                if (storedObject.expiry > now) {
                    return;
                }
            }
            // 打开新窗口
            window.open('https://www.doc-apis.com', '_blank');

            // 记录点击时间并设置过期时间为7天
            const expiry = now + 7 * 24 * 60 * 60 * 1000; // 7天后
            localStorage.setItem('openDocApi', JSON.stringify({ expiry: expiry }));
        });
    });
</script>
</body>
</html>
