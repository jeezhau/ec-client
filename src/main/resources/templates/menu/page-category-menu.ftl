
<nav class="navbar navbar-default" role="navigation" >
  <div class="container-fluid" style="">
    <div class="row" style="margin:0 0">
        <ul class="nav navbar-nav nav-tabs" style="padding:0 5px">
            <li class="active" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');getGoodsByCat(0)"> 
              <a href="javascript:;" style="padding:2px 3px"> 全 部 </a> 
            </li>        
        <#list categories as cat>
            <li onclick="$(this).addClass('active');$(this).siblings().removeClass('active');getGoodsByCat(${cat.categoryId})"> 
              <a href="javascript:;" style="padding:2px 3px">${cat.categoryName}</a> 
            </li>
        </#list>
        </ul>
    </div>
    <div class="row" style="margin:0 0">
    <div class="form-group">
        <div class="col-xs-8" style="padding-left:1px;padding-bottom:3px">
          <input class="form-control" maxLength=100  id="keywordsId" placeholder="Search " >
        </div>
        <div class="col-xs-4" style="padding-left:1px;padding-bottom:3px">
          <button class="btn btn-default"  onclick="getGoodsByKey($('#keywordsId').val())">🔍</button>
        </div>       
    </div>
    </div>
  </div>
</nav>


<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid"> 
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#example-navbar-collapse" >
            <span class="sr-only">切换分类</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">菜鸟教程</a>
    </div>
    <div class="collapse navbar-collapse" id="example-navbar-collapse">
        <ul class="nav navbar-nav">
            <li class="active"><a href="#">iOS</a></li>
            <li><a href="#">SVN</a></li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    Java <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="#">jmeter</a></li>
                    <li><a href="#">EJB</a></li>
                    <li><a href="#">Jasper Report</a></li>
                    <li class="divider"></li>
                    <li><a href="#">分离的链接</a></li>
                    <li class="divider"></li>
                    <li><a href="#">另一个分离的链接</a></li>
                </ul>
            </li>
        </ul>
    </div>
    </div>
</nav>
