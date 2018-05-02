
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
          <button class="btn btn-default"  onclick="getGoodsByKey($('#keywordsId').val())">🔍</button
        </div>       
    </div>
    </div>
  </div>
</nav>
