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
        Schema::table('users', function (Blueprint $table) {
            $table->string('username', 50)->unique()->after('id');
            $table->string('phone', 15)->nullable()->after('email');
            $table->string('full_name', 100)->nullable()->after('phone');
            $table->enum('role', ['admin', 'inspector', 'staff', 'customer'])->default('customer')->after('full_name');
            $table->foreignId('inspection_center_id')->nullable()->constrained('centers')->onDelete('set null')->after('role');
            $table->enum('status', ['active', 'inactive', 'suspended'])->default('active')->after('inspection_center_id');
            $table->softDeletes()->after('updated_at');
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
            $table->dropColumn([
                'username',
                'phone',
                'full_name',
                'role',
                'inspection_center_id',
                'status',
                'deleted_at'
            ]);
        });
    }
};
