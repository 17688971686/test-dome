/**
 * 百度地图工具类
 * @author：lqs
 * @date：2017-11-06
 * */
angular.module('baidumap.service', ['global_variable'])
.service('baidumMpUtil',baidumMpUtil);
baidumMpUtil.$inject = ['global'];
function baidumMpUtil(global){
	
	var service = {
		drawArrow:drawArrow
	};
	
	
	/**
	 * 画箭头
	 * @param map 地图实例
	 * @param polyline 折线数组
	 * @param length 箭头长度
	 * @param angleValue 箭头角度
	 * */
	function drawArrow(map,polyline,length,angleValue){
		if(!map){
			return ;
		}
		var linePoint = polyline.getPath();
		var arrowCount = linePoint.length;  
		for(var i = 1;i < arrowCount;i++){ 
			var pixelStart = map.pointToPixel(linePoint[i-1]);  
			var pixelEnd = map.pointToPixel(linePoint[i]);  
			var angle = angleValue;
			var r = length; // r/Math.sin(angle)代表箭头长度  
			var delta = 0; //主线斜率，垂直时无斜率  
			var param = 0; 
			var pixelTemX,pixelTemY;
			var pixelX,pixelY,pixelX1,pixelY1;//箭头两个点  
			if(pixelEnd.x-pixelStart.x==0){ //斜率不存在是时  
			    pixelTemX = pixelEnd.x;  
			    if(pixelEnd.y > pixelStart.y)  
			    {  
			    	pixelTemY = pixelEnd.y-r;  
			    }  
			    else  
			    {  
			    	pixelTemY = pixelEnd.y+r;  
			    }     
			    //已知直角三角形两个点坐标及其中一个角，求另外一个点坐标算法  
			    pixelX = pixelTemX-r*Math.tan(angle);   
			    pixelX1 = pixelTemX+r*Math.tan(angle);  
			    pixelY = pixelY1=pixelTemY;  
			}  
			else  //斜率存在时  
			{  
			    delta = (pixelEnd.y-pixelStart.y)/(pixelEnd.x-pixelStart.x);  
			    param = Math.sqrt(delta*delta+1);  
			  
			    if((pixelEnd.x-pixelStart.x)<0) //第二、三象限  
			    {  
				    pixelTemX = pixelEnd.x+ r/param;  
				    pixelTemY = pixelEnd.y+delta*r/param;  
			    }  
			    else//第一、四象限  
			    {  
				    pixelTemX = pixelEnd.x- r/param;  
				    pixelTemY = pixelEnd.y-delta*r/param;  
			    }  
			    //已知直角三角形两个点坐标及其中一个角，求另外一个点坐标算法  
			    pixelX = pixelTemX+ Math.tan(angle)*r*delta/param;  
			    pixelY = pixelTemY-Math.tan(angle)*r/param;  
			  
			    pixelX1 = pixelTemX- Math.tan(angle)*r*delta/param;  
			    pixelY1 = pixelTemY+Math.tan(angle)*r/param;  
			}  
			var pointArrow = map.pixelToPoint(new BMap.Pixel(pixelX,pixelY));  
			var pointArrow1 = map.pixelToPoint(new BMap.Pixel(pixelX1,pixelY1));  
			var arrow = new BMap.Polyline([ pointArrow,linePoint[i], pointArrow1 ],
				{strokeColor:global.map.arrow.strokeColor, strokeWeight:global.map.arrow.strokeWeight, strokeOpacity:global.map.arrow.strokeOpacity});			
			map.addOverlay(arrow);  
		}    
	}
	
	return service;
}
