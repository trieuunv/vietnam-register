<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\DB;
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
        Schema::table('users', function (Blueprint $table) {
            // 1. Tạo cột mới
            $table->unsignedBigInteger('center_id')->nullable()->after('inspection_center_id');
        });

        // 2. Copy dữ liệu từ inspection_center_id sang center_id
        DB::statement('UPDATE users SET center_id = inspection_center_id');

        Schema::table('users', function (Blueprint $table) {
            // 3. Xóa foreign key cũ
            $table->dropForeign(['inspection_center_id']);

            // 4. Xóa cột cũ
            $table->dropColumn('inspection_center_id');

            // 5. Thêm foreign key mới
            $table->foreign('center_id')->references('id')->on('centers')->onDelete('cascade');
        });

        Schema::table('users', function (Blueprint $table) {
            $table->enum('center_role', ['admin', 'inspector', 'staff'])->nullable();
            $table->json('center_permissions')->nullable();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('users', function (Blueprint $table) {
            $table->unsignedBigInteger('inspection_center_id')->nullable()->after('center_id');
        });

        DB::statement('UPDATE users SET inspection_center_id = center_id');

        Schema::table('users', function (Blueprint $table) {
            $table->dropForeign(['center_id']);
            $table->dropColumn('center_id');

            $table->foreign('inspection_center_id')->references('id')->on('centers')->onDelete('cascade');
        });

        Schema::table('users', function (Blueprint $table) {
            $table->dropColumn('center_role');
            $table->dropColumn('center_permissions');
        });
    }
};
