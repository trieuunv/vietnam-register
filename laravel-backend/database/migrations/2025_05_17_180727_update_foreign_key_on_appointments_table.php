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
        Schema::table('appointments', function (Blueprint $table) {
            // Đảm bảo inspection_id là nullable
            $table->unsignedBigInteger('inspection_id')->nullable()->change();

            // Xóa foreign key cũ
            $table->dropForeign(['inspection_id']);

            // Thêm lại foreign key với SET NULL
            $table->foreign('inspection_id')
                ->references('id')->on('inspections')
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
        Schema::table('appointments', function (Blueprint $table) {
            // Xóa foreign key mới
            $table->dropForeign(['inspection_id']);

            // Thêm lại foreign key không có onDelete
            $table->foreign('inspection_id')
                ->references('id')->on('inspections');
        });
    }
};
