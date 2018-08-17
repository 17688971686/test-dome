var gulp = require('gulp'),
    jshint = require('gulp-jshint'),
    uglify = require('gulp-uglify'),
    rename = require('gulp-rename'),
    clean = require('gulp-clean'),
    concat = require('gulp-concat'),
    plumber = require('gulp-plumber')//,
    // notify = require('gulp-notify'),
    // cache = require('gulp-cache'),
    // livereload = require('gulp-livereload')
;

// JS处理任务
gulp.task('app', function () {
    console.log("build sn app");
    return gulp.src(['src/main/resources/static/app/**/*.js', '!src/main/resources/static/app/common/*.js'])          //引入所有需处理的JS
        .pipe(plumber())
        .pipe(jshint.reporter('default'))                           //JS代码检查
        .pipe(concat('app.all.js'))                                 //合并JS文件
        .pipe(gulp.dest('src/main/resources/static/app-dist'))       //完整版输出
        .pipe(rename({suffix: '.min'}))                             //重命名
        .pipe(uglify())                                             //压缩JS
        .pipe(gulp.dest('src/main/resources/static/app-dist'))       //压缩版输出
        // .pipe(notify({message: 'JS文件处理完成'}))
        ;
});

gulp.task('appCommon', function () {
    console.log('build sn common');
    return gulp.src(['src/main/resources/static/app/common/*.js'])          //引入所有需处理的JS
        .pipe(plumber())
        .pipe(jshint.reporter('default'))                           //JS代码检查
        .pipe(concat('app.common.all.js'))                                 //合并JS文件
        .pipe(gulp.dest('src/main/resources/static/app-dist'))       //完整版输出
        .pipe(rename({suffix: '.min'}))                             //重命名
        .pipe(uglify())                                             //压缩JS
        .pipe(gulp.dest('src/main/resources/static/app-dist'))       //压缩版输出
        // .pipe(notify({message: 'JS文件处理完成'}))
        ;
});


// 目标目录清理
gulp.task('clean', function () {
    return gulp.src(['src/main/resources/static/app-dist'], {read: false}).pipe(plumber()).pipe(clean());
});

// 预设任务，执行清理后，
gulp.task('default', ['clean'], function () {
    gulp.start('app');

    gulp.start('appCommon');
});

// 文档临听
gulp.task('watch', ["default"], function () {

    // 监听所有.js档
    gulp.watch(['src/main/resources/static/app/common/*.js'], ['appCommon']);

    gulp.watch(['src/main/resources/static/app/**/*.js', '!src/main/resources/static/app/common/*.js'], ['app']);

});
