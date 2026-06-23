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
        Schema::table('criteria_results', function (Blueprint $table) {
            // Xóa foreign key cũ
            $table->dropForeign(['inspection_id']);

            // Thêm lại foreign key mới với ON DELETE CASCADE
            $table->foreign('inspection_id')
                ->references('id')->on('inspections')
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
        Schema::table('criteria_results', function (Blueprint $table) {
            // Xóa foreign key mới
            $table->dropForeign(['inspection_id']);

            // Thêm lại foreign key không có cascade
            $table->foreign('inspection_id')
                ->references('id')->on('inspections');
        });
    }
};
