<!-- 卖家售后回复信息 -->
  <div class="row" style="margin:8px 0px 3px 0px;" onclick="">
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">卖家售后</span>
    </div>
    <div v-for="item in aftersaleResult" class="row" style="margin:1px 0px;padding:0 20px;background-color:white;">
     <div class="row">
       <span class="pull-right">{{item.type}}</span>
       <span class="pull-left">{{item.time}}</span>
     </div>
     <div class="row">
       <p>{{item.content.reason}}</p>
       <p v-if="item.content.dispatchMode" style="color:blue">
       卖家发货物流：{{getDispatchMode(item.content.dispatchMode)}} {{item.content.logisticsComp}} {{item.content.logisticsNo}}
       </p>
       <p v-if="item.content.recvAddr" style="color:blue">
       卖家退回地址：{{item.content.recvName}} {{item.content.recvPhone}} {{item.content.recvAddr}}
       </p>
     </div>
    </div>
  </div> 