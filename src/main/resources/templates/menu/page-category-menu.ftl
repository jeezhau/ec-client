
<nav class="navbar navbar-default" role="navigation" >
  <div class="container-fluid" style="">
    <div class="row" style="margin:0 0">
        <ul class="nav navbar-nav nav-tabs" style="padding:0 5px">
        <#list categories as cat>
            <li onclick="$(this).addClass('active');$(this).siblings().removeClass('active');getGoodsByCat(${cat.id})"> 
              <a href="javascript:;" style="padding:2px 3px">${cat.name}</a> 
            </li>
        </#list>
        </ul>
    </div>
    <div class="row" style="margin:0 0">
    <div class="form-group">
        <div class="col-xs-8" style="padding-left:1px;padding-bottom:3px">
          <input class="form-control" v-model="param.legalPername"  maxLength=100   placeholder="Search " >
        </div>
        <div class="col-xs-4" style="padding-left:1px;padding-bottom:3px">
          <button class="btn btn-default" >🔍</button
        </div>       
    </div>
    </div>
  </div>
</nav>
