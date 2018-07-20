<!-- 买家售后申请信息 -->
  <div class="row" style="margin:8px 0px 3px 0px;" onclick="">
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">买家售后</span>
    </div>
    <div v-for="item in aftersaleReason" class="row" style="margin:1px 0px;padding:0 20px;background-color:white;">
     <div class="row">
       <span class="pull-right">{{item.type}}</span>
       <span class="pull-left">{{item.time}}</span>
     </div>
     <div class="row">
       <p>{{item.content.reason}}</p>
       <p v-if="item.content.dispatchMode" style="color:blue">
       买家退货物流：{{getDispatchMode(item.content.dispatchMode)}} {{item.content.logisticsComp}} {{item.content.logisticsNo}}
       </p>
     </div>
    </div>
  </div>