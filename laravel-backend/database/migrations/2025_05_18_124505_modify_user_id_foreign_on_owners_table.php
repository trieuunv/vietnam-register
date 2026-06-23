<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('owners', function (Blueprint $table) {
            // Đảm bảo cột user_id nullable nếu dùng SET NULL
            $table->unsignedBigInteger('user_id')->nullable()->change();

            // Xóa foreign key hiện tại
            $table->dropForeign(['user_id']);

            // Tạo lại foreign key với ON DELETE SET NULL
            $table->foreign('user_id')
                ->references('id')->on('users')
                ->onDelete('set null');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('owners', function (Blueprint $table) {
            // Xóa foreign key hiện tại
            $table->dropForeign(['user_id']);

            // Tạo lại foreign key với CASCADE
            $table->foreign('user_id')
                ->references('id')->on('users')
                ->onDelete('cascade');
        });
    }
};
