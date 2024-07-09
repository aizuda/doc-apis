<!-- licence1: Apache-2.0 licence2: AGPL-3.0  from1: japidoc from2:doc-apis originated -->
<h2 id="${requestNode.methodName}"><a href="#">${(requestNode.description)!''} <#if requestNode.deprecated><span
                class="badge">${i18n.getMessage('deprecated')}</span></#if></a></h2>
<#if requestNode.supplement??>
    <p class="text-muted">${requestNode.supplement}</p>
</#if>
<#if requestNode.author??>
    <p class="text-muted"><em>${i18n.getMessage('author')}: ${requestNode.author}</em></p>
</#if>
<p class="title-wrap">
    <strong>${i18n.getMessage('requestUrl')}</strong>
    <span class="inner-header">Header <i class="fa fa-gear" aria-hidden="true"></i></span>
</p>
<p>
    <code class="innerUrl">${requestNode.url}</code>
    <#list requestNode.method as method>
        <#if method == 'GET'>
            <span class="label bg-green">${method}</span>
        <#elseif method == 'POST'>
            <span class="label bg-blue">${method}</span>
        <#elseif method == 'PUT'>
            <span class="label bg-orange">${method}</span>
        <#else>
            <span class="label bg-red">${method}</span>
        </#if>
    </#list>
    <#if requestNode.changeFlag == 1>
        <span class="label label-success">${i18n.getMessage('new')}</span>
    <#elseif requestNode.changeFlag == 2>
        <span class="label label-warning">${i18n.getMessage('modify')}</span>
    </#if>
</p>
<#if requestNode.paramNodes?size != 0>
    <#assign requestJsonBody = ''/>
    <#list requestNode.paramNodes as paramNode>
        <#if paramNode.jsonBody>
            <#assign requestJsonBody = paramNode.description/>
        </#if>
    </#list>
    <#if requestJsonBody == '' || (requestJsonBody != '' && requestNode.paramNodes?size gt 1)>
        <p><strong>${i18n.getMessage('requestParameters')}</strong> <span class="badge">application/x-www-form-urlencoded</span></p>
        <table class="table table-bordered">
            <tr>
                <th>${i18n.getMessage('parameterName')!''}</th>
                <th>${i18n.getMessage('parameterType')!''}</th>
                <th>${i18n.getMessage('parameterNeed')!''}</th>
                <th>${i18n.getMessage('description')!''}</th>
            </tr>
            <#list requestNode.paramNodes as paramNode>
                <#if !(paramNode.jsonBody)>
                    <tr>
                        <td>${paramNode.name!''}</td>
                        <td>${paramNode.type!''}</td>
                        <td>${paramNode.required?string(i18n.getMessage('yes'),i18n.getMessage('no'))}</td>
                        <td>${(paramNode.description)!''}</td>
                    </tr>
                </#if>
            </#list>
        </table>
    </#if>
    <#if requestJsonBody != ''>
        <p><strong>${i18n.getMessage('requestBody')}</strong> <span class="badge">application/json</span></p>
    <div class="curl-wrap">
        <pre class="prettyprint lang-json in-params">${requestJsonBody}</pre>
        <em><i class="fa fa-copy" aria-hidden="true"></i>copy</em>
    </div>
    </#if>
</#if>
<#if requestNode.responseNode??>
    <p><strong>${i18n.getMessage('responseResult')}</strong></p>
    <div class="curl-wrap">
        <pre class="prettyprint lang-json">${requestNode.responseNode.toJsonApi()}</pre>
        <em><i class="fa fa-copy" aria-hidden="true"></i>copy</em>
    </div>
    <#if requestNode.androidCodePath??>
        <div class="form-group fun-btn-wrap">
            <div class="phone-btn-wrap">
                <a type="button" class="btn btn-sm btn-default" href="${requestNode.androidCodePath}"><i class="fa fa-android" aria-hidden="true"></i> Android Model</a>
                <a type="button" class="btn btn-sm btn-default" href="${requestNode.iosCodePath}"><i class="fa fa-apple" aria-hidden="true"></i>iOS Model</a>
            </div>
            <div class="debug-btn-wrap">
                <a type="button" class="btn btn-sm btn-default" ><i class="fa fa-bug" aria-hidden="true"></i><span>debug</span></a>
            </div>
        </div>
        <div class="debug-area">
            <div class="debug-title">
                <h4>Parameters</h4>
            </div>
            <!-- application/x-www-form-urlencoded -->
            <textarea rows="8" class="debug-textarea"></textarea>
            <!-- application/json -->
            <div class="debug-params">
                <em></em>
                <span>Name</span>
                <span>Description</span>
            </div>
            <div class="params-form"></div>
            <div class="execute">Execute</div>

            <div class="spinner" id="loadingSpinner"></div>

            <div class="responses-wrap">
                <div class="debug-title">
                    <h4>Curl</h4>
                </div>
                <div class="curl-wrap">
                    <pre class="prettyprint lang-json curl-area"></pre>
                    <em><i class="fa fa-copy" aria-hidden="true"></i>copy</em>
                </div>
                <div class="debug-title">
                    <h4>Responses</h4>
                </div>
                <div class="debug-responses">
                    <span>Code</span>
                    <span>Response body</span>
                </div>
                <div class="debug-response-wrap">
                    <span class="response-code"></span>
                    <pre class="prettyprint lang-json responses-area"></pre>
                </div>
            </div>
        </div>
    </#if>
</#if>

