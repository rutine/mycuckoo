<%@ page language="java" pageEncoding="utf-8"%>
<script src="${ctx}/static/jquery/1.7.2/jquery.min.js" type="text/javascript"></script>
<script src="${ctx}/static/bootstrap/3.2.0/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctx}/static/ztree/3.5/js/ztree.core.min.js" type="text/javascript"></script>
<script src="${ctx}/static/ztree/3.5/js/ztree.excheck.min.js" type="text/javascript"></script>
<script src="${ctx}/static/webuploader/0.1.5/js/webuploader.js" type="text/javascript"></script>
<script src="${ctx}/static/js/util/commUtils.js" type="text/javascript"></script>
<script src="${ctx}/static/js/util/BasicConstant.js" type="text/javascript"></script>
<script type="text/javascript">
	jQuery(function($) {
		// 导航菜单
		var $navbar = $('.navbar ul.nav:not(.navbar-right) > li:has(a)');
		$navbar.click(function() {
			if ($(this).hasClass('active')) return;
			var navIdForShow = $(this).children('a').attr('href');
			var navIdForHide = "";
			$navbar.each(function() {
				if ($(this).hasClass('active')) {
					navIdForHide = $(this).children('a').attr('href');
					$(this).removeClass('active');
				}
			});
			$(this).addClass('active');
			$(navIdForHide).hide('normal', 'swing');
			$(navIdForShow).show('normal', 'linear');
		});
		// 侧边菜单收缩/展开
		var $mycuckooSidebar = $('.mycuckoo-sidebar');
		var $sidebarFirstLi = $mycuckooSidebar.find('.mycuckoo-sidenav > li');
		var $sidebarSecondLi = $mycuckooSidebar.find('.nav .nav > li');
		$mycuckooSidebar.on('click', '.mycuckoo-sidenav > li > a', function() {
			var $this = $(this);
			$this.next().slideToggle('normal', 'swing');
			$this.children('span').toggleClass('glyphicon-chevron-left');
			$this.children('span').toggleClass('glyphicon-chevron-down');
		});
		// 侧边菜单点击蓝色高亮
		$mycuckooSidebar.on('click', '.nav .nav > li > a', function() {
			var $this = $(this);
			$sidebarFirstLi.removeClass('active');
			$sidebarSecondLi.removeClass('active');
			$this.parent().addClass('active');
			$this.parents('li').addClass('active');
			/* var json = $this.attr('href');
			json = json.substring(1);
			json = json.replace(/%/g, '"');
			json = $.parseJSON(json);
			// 加载内容页面 
			var id = json.belongToSys + '-' + json.id;
			var $page = $('#' + id);
			if(!$page.length) {
				var availHeight = window.screen.availHeight - 219;
				$('.mycuckoo-main').children().addClass('hidden').removeClass('active');
				$page = $('<div class="active">' + 
								'<iframe width="100%" allowfullscreen="true" frameborder="0"></iframe>' + 
							'</div>');
				$page.attr('id', id);
				$page.appendTo($('.mycuckoo-main'));
				$page.find('iframe').attr('src', '${ctx}' + BasicConstant.ACTION_MAP.get(id) + "?id=" + json.id);
				$page.find('iframe').attr('height', availHeight);
			} else {
				$('.mycuckoo-main').children().addClass('hidden').removeClass('active');
				$page.removeClass('hidden').addClass('active');
			} */
		});
		
		// 初始化菜单选择
		var id = $mycuckooSidebar.find('li.active').parent('ul').show()
										.parent('li').addClass('active')
										.find('a > span').removeClass('glyphicon-chevron-left').addClass('glyphicon-chevron-down').end()
										.siblings('li').children('ul').hide().end()
										.find('a > span').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-left').end()
										.parent('ul')
										.parent('div').show()
										.siblings('div').hide().end()
										.attr('id');
		$navbar.find('a[href=#' + id + ']').parent('li').addClass('active').siblings('li').removeClass('active');
		
		
		// 个人中心
		var $userContent = $('<form class="form photo-upload">' + 
						'<div class="photo fade in">' + 
						'	<div class="error fade">上传失败!</div>' + 
						'	<div class="preview thumbnail"><img width="110" height="110" src=""/></div>' + 
						'	<div class="progress hidden">' + 
						'		<div class="progress-bar progress-bar-success progress-bar-striped" role="progressbar" ' + 
						'				aria-valuemin="0" aria-valuemax="100" aria-valuenow="10" style="width:10%;"></div>' + 
						'	</div>' + 
						'	<div class="btn-group btn-group-sm hidden">' + 
						'		<button class="btn btn-primary btn-start" data-loading-text="上传中"><span class="glyphicon glyphicon-upload"></span>上传</button>' + 
						'		<button class="btn btn-danger btn-cancel"><span class="glyphicon glyphicon-ban-circle"></span>取消</button>' + 
						'	</div>' + 
						'</div>' + 
						'<div id="photo_picker" class="fade"><span class="glyphicon glyphicon-picture"><span>上传头像</div>' + 
						'<a href="${ctx}/login/logout" class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-log-out"></span> 退出登录</a>' + 
					'</form>');
		var uploader = null;
		var userPhotoUrl = $('.navbar .user').attr('data-photo-url');
		
		// 判断用户是否有头像
		if(userPhotoUrl) {
			$userContent.find('img').attr('src', userPhotoUrl);
		} else {
			$userContent.find('.photo').removeClass('in');
			$userContent.find('#photo_picker').addClass('in');
		}
		
		$('.navbar .user').popover({
			html : true, 
			placement : 'bottom',
			content : $userContent,
			trigger : 'click',
			title : '个人信息'
		}).on('shown.bs.popover', function() {
			uploader = WebUploader.create({
				swf: '${ctx}/webuploader/0.1.5/js/Uploader.swf',
				server: '${ctx}/uum/userMgr/uploadPhoto',
				pick: $('.navbar #photo_picker'),
				
				resize: false,
				accept : {
					title : 'Images',
					extensions : 'gif,jpg,jpeg,bmp,png',
					mimeTypes : 'image/*'
				},
			});
			var $photoUpload = $('.navbar .photo-upload');
			var $photoPicker = $photoUpload.find('#photo_picker');
			var $error = $photoUpload.find('.error');
			var $photo = $photoUpload.find('.photo');
			var $img = $photoUpload.find('img');
			var $progress = $photoUpload.find('.progress');
			var $btnGroup = $photoUpload.find('.btn-group');
			var $btnStart = $btnGroup.find('.btn-start').off();
			var $btnCancel = $btnGroup.find('.btn-cancel').off();
			
			$btnCancel.on('click', function() {
				uploader.stop(true);
				$photo.removeClass('in');
				$photoPicker.addClass('in');
			});
			$btnStart.on('click', function() {
				$btnStart.button('loading');
				uploader.upload();
			});
			
			uploader.on('beforeFileQueued', function() {
				uploader.reset();
				
				$btnStart.button('reset');
				$error.removeClass('in');
				$btnGroup.removeClass('hidden');
			});
			uploader.on('fileQueued', function(file) {
				// 创建缩略图
				uploader.makeThumb(file, function(error, src) {
					if (error) {
						$error.addClass('in').text('不能预览');
						return;
					}
					$img.attr('src', src);
					$photo.addClass('in');
					$photoPicker.removeClass('in');
				}, 110, 110);
			});
			uploader.on('uploadProgress', function(file, percentage) {
				$progress.removeClass('hidden').find('.progress-bar').css('width', parseInt(percentage * 100) + '%');
			});
			uploader.on('uploadSuccess', function(file, json) {
				$btnGroup.addClass('hidden');
			});
			uploader.on('uploadError', function(file) {
				$error.addClass('in').text('上传失败!');
			});
			uploader.on('uploadComplete', function(file) {
				$btnStart.button('reset');
				$progress.addClass('hidden');
			});
		}).on('hidden.bs.popover', function() {
			uploader.destroy();
		});
		
	});
</script>