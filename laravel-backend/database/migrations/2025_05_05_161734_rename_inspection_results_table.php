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
        Schema::table('inspection_results', function (Blueprint $table) {
            $table->dropForeign(['inspection_id']);
            $table->dropForeign(['criteria_id']);
            $table->dropForeign(['inspector_id']);
        });

        Schema::rename('inspection_results', 'criteria_results');

        Schema::table('criteria_results', function (Blueprint $table) {
            $table->foreign('inspection_id')->references('id')->on('inspections');
            $table->foreign('criteria_id')->references('id')->on('inspection_criteria');
            $table->foreign('inspector_id')->references('id')->on('users');
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
            $table->dropForeign(['inspection_id']);
            $table->dropForeign(['criteria_id']);
            $table->dropForeign(['inspector_id']);
        });

        Schema::rename('criteria_results', 'inspection_results');

        Schema::table('inspection_results', function (Blueprint $table) {
            $table->foreign('inspection_id')->references('id')->on('inspections');
            $table->foreign('criteria_id')->references('id')->on('inspection_criteria');
            $table->foreign('inspector_id')->references('id')->on('users');
        });
    }
};
