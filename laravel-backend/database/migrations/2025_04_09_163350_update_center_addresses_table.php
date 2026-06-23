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
        Schema::table('center_addresses', function (Blueprint $table) {
            $table->dropColumn(['province_id', 'district_id', 'ward_id']);

            $table->integer('province_code')->nullable();
            $table->integer('district_code')->nullable();
            $table->integer('ward_code')->nullable();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('center_addresses', function (Blueprint $table) {
            $table->dropColumn(['province_code', 'district_code', 'ward_code']);

            $table->integer('province_id')->nullable();
            $table->integer('district_id')->nullable();
            $table->integer('ward_id')->nullable();
        });
    }
};
