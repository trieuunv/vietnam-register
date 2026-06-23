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
        Schema::create('citizen_cards', function (Blueprint $table) {
            $table->id();
            $table->foreignId('owner_id')->constrained('owners');
            $table->string('citizen_id', 20);
            $table->string('nationality')->default('Vietnamese');
            $table->string('place_of_origin', 255)->nullable();
            $table->date('date_of_issue')->nullable();
            $table->string('place_of_issue', 255)->nullable();
            $table->string('image_url', 255)->nullable();
            $table->timestamps();
            $table->softDeletes();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('citizen_cards');
    }
};
