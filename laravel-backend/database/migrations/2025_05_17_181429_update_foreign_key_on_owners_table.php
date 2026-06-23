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
            // Xóa foreign key cũ
            $table->dropForeign(['user_id']);

            // Tạo lại với cascade
            $table->foreign('user_id')
                ->references('id')->on('users')
                ->onDelete('cascade');
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
            // Xóa foreign key cascade
            $table->dropForeign(['user_id']);

            // Tạo lại ràng buộc cũ (không cascade)
            $table->foreign('user_id')
                ->references('id')->on('users');
        });
    }
};
