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
        Schema::table('citizen_cards', function (Blueprint $table) {
            // Xóa ràng buộc khóa ngoại cũ
            $table->dropForeign(['owner_id']);

            // Tạo lại với ON DELETE CASCADE
            $table->foreign('owner_id')
                ->references('id')->on('owners')
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
        Schema::table('citizen_cards', function (Blueprint $table) {
            // Xóa ràng buộc mới
            $table->dropForeign(['owner_id']);

            // Tạo lại khóa ngoại cũ (không cascade)
            $table->foreign('owner_id')
                ->references('id')->on('owners');
        });
    }
};
