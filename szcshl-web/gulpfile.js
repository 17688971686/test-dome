var gulp = require('gulp'),
    jshint = require('gulp-jshint'),
    uglify = require('gulp-uglify'),
    rename = require('gulp-rename'),
    clean = require('gulp-clean'),
    concat = require('gulp-concat'),
    plumber = require('gulp-plumber'),
    notify = require('gulp-notify')//,
    // cache = require('gulp-cache'),
    // livereload = require('gulp-livereload')
;

// JS处理任务
gulp.task('scripts', function () {
    return gulp.src('src/main/webapp/contents/app/**/*.js')          //引入所有需处理的JS
        .pipe(plumber())
        .pipe(jshint.reporter('default'))   //S代码检查
        .pipe(concat('app.all.js'))         //合并JS文件
        .pipe(gulp.dest('src/main/webapp/contents/app-dist'))        //完整版输出
        .pipe(rename({suffix: '.min'}))     //重命名
        .pipe(uglify())                     //压缩JS
        .pipe(gulp.dest('src/main/webapp/contents/app-dist'))        //压缩版输出
        .pipe(notify({message: 'JS文件处理完成'}));
});


// 目标目录清理
gulp.task('clean', function () {
    return gulp.src(['src/main/webapp/contents/app-dist'], {read: false}).pipe(plumber()).pipe(clean());
});

// 预设任务，执行清理后，
gulp.task('default', ['clean'], function () {
    gulp.start(/*'styles',*/ 'scripts'/*, 'images'*/);
});

// 文档临听
gulp.task('watch', function () {

//  // 监听所有.scss文档
//   gulp.watch('src/main/webapp/contents/styles/**/*.scss', ['styles']);

    // 监听所有css文档
    // gulp.watch('src/main/webapp/contents/css/*.css', ['styles']);

    // 监听所有.js档
    gulp.watch('src/main/webapp/contents/app/**/*.js', ['scripts']);

    // 监听所有图片档
    // gulp.watch('src/main/webapp/contents/app/images/*', ['images']);

//   // 创建实时调整服务器 -- 在项目中未使用注释掉
//   var server = livereload();
//   // 监听 dist/ 目录下所有文档，有更新时强制浏览器刷新（需要浏览器插件配合或按前文介绍在页面增加JS监听代码）
//   gulp.watch(['src/main/webapp/contents/app-dist/**']).on('change', function(file) {
//     server.changed(file.path);
//   });

});
